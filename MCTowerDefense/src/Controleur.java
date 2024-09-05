import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Controleur {

    private Overworld world;

    private Interface ihm;

    private PauseMenu pauseMenu;

    private InfosMenu infosMenu;

    private Thread tdProjectile;

    private Thread animationProjectiles;

    private Traps traps;

    private Shop shop;

    private Book book;

    private char[] dirForMobs;

    public Controleur(Overworld world, Interface ihm)
    {
        this.world = world;
        this.ihm = ihm;
        this.pauseMenu = new PauseMenu(this.getIhm(), this.getWorld());
        this.infosMenu = new InfosMenu(this.getIhm(), this.getWorld());
        this.dirForMobs = new char[100];
        this.traps = new Traps(getWorld(), getIhm(), getInfosMenu());
        this.shop = new Shop(getWorld(), getIhm(), getInfosMenu());
        this.book = new Book(getWorld(), getIhm(), getInfosMenu());
        this.start();
    }

    public Overworld getWorld()
    {
        return this.world;
    }

    public Interface getIhm()
    {
        return this.ihm;
    }

    public InfosMenu getInfosMenu()
    {
        return this.infosMenu;
    }



    private void animation(int x, int y, char dir)
    {

        if ( getWorld().isInHouse() ) //Si il est dans la maison on annule
        {
            return;
        }


        // Définir les points de départ et d'arrivée
        int xDep = (getWorld().getPlayer().getPosX()* 50) + 50;
        int yDep = (getWorld().getPlayer().getPosY()* 50) + 50;
        int xArr = (x*50) + 50;
        int yArr = (y*50) + 50;

        // Durée de l'animation en millisecondes
        int animationDuration = 500;

        Timer timer = new Timer(15, new ActionListener()
        {
            private long startTime = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }




                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;



                if (elapsedTime >= animationDuration) {
                    // Arrêter le timer une fois que l'animation est terminée et repositionner les éléments correctement
                    getWorld().getPlayer().setInAnimation(false);
                    ((Timer) e.getSource()).stop();
                    return;
                }


                // Calculer la position intermédiaire en fonction du temps écoulé
                float progress = (float) elapsedTime / animationDuration;
                int xFinal = (int) (xDep + progress * (xArr - xDep));
                int yFinal = (int) (yDep + progress * (yArr - yDep));



                //On attribu ici les coordonnées de déplacement
                getWorld().getPlayer().setPosXDeplacement(xFinal);
                getWorld().getPlayer().setPosYDeplacement(yFinal);

            }
        });

        // Démarrer le timer
        timer.start();


        //On met les nouvelles coordonnées au joueur
        this.getWorld().getPlayer().setPosX(x);
        this.getWorld().getPlayer().setPosY(y);

        //On place un bloc de sable à l'endroit ou va le joueur
        this.getWorld().setMap(x, y, 1);

        if ( getWorld().getPlayer().getPosX() == 11 && getWorld().getPlayer().getPosY() == 3)
        {
            getWorld().setInHouse(true);
            Cursor inHouseCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/wood.png")).getImage() , new Point(0, 0), "Curseur fireBallTrap");
            getIhm().setCursor(inHouseCursor);
        }
    }


    public char[] getDirForMobs()
    {
        return dirForMobs;
    }

    public Runnable timerStart() //Lancement de la partie après 60 sec
    {
        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!getPauseMenu().isOpen())//Si le jeu n'est pas en pause
                {
                    if(getWorld().getTime() > 0)
                    {
                        getWorld().setTime(-1);
                    }else{
                        for(int i=0 ; getWorld().getTabTower()[i] != null; i++)
                        {
                            getWorld().getTabTower()[i].dropTabProj(); //On enlève tout les projectiles du tableau de toutes les tours
                        }
                        getWorld().initMobs( getWorld().getWave() ); //Génération du nombre de mobs

                        Thread spawnMobs = new Thread(spawnMobs());//Quand le timer est à 0 on lance la vague
                        spawnMobs.start();

                        if( getWorld().getWave() == 1 )
                        {
                            animationProjectiles = new Thread(animationDefenses()); //On commence à tirer
                            animationProjectiles.start();
                        }

                        ((Timer) e.getSource()).stop();
                    }
                }
            }
        });

        // Démarrer le chronomètre
        timer.start();
        return null;
    }

    public PauseMenu getPauseMenu()
    {
        return pauseMenu;
    }

    public void setDirForMobs(int id, char dir)
    {
        if(dir == 'E' || dir== 'O' || dir =='S' || dir=='N')
        {
            this.dirForMobs[id] = this.getWorld().getPlayer().getDir();
        }
    }

    public Runnable refreshIhm() //Met 60 fps
    {
        Timer fps = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getIhm().reload(getWorld());
                getInfosMenu().drawInfos();
            }
        });
        fps.start();
        return null;
    }


    private void start()
    {
        timerStart();

        ClicksEvent();

        Thread refreshIhm = new Thread(refreshIhm());
        refreshIhm.start();

        //Quand on clic on recupère les coordonnées
        this.getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getIhm().isMenuOpen() || getWorld().getPlayer().isAnimation() || getWorld().isInHouse() || getShop().isOpenned() || getTraps().isOpenned())
                {
                    return;
                }

                int x = ((e.getX() + 50 ) / 50 ) - 2;
                int y = ((e.getY() + 20 ) / 50 ) - 2;
                char dir;



                if ( getWorld().getPlayer().getPosX() > x && getWorld().getPlayer().getPosY() == y)
                {
                    dir = 'O';
                }else if ( getWorld().getPlayer().getPosX() < x && getWorld().getPlayer().getPosY() == y)
                {
                    dir = 'E';
                } else if ( getWorld().getPlayer().getPosY() > y && getWorld().getPlayer().getPosX() == x)
                {
                    dir = 'N';
                }else if ( getWorld().getPlayer().getPosY() < y && getWorld().getPlayer().getPosX() == x){
                    dir = 'S';
                }else{
                    dir = 'E';
                }

                if((x == 0 && y == 0 && !((getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 1) || (getWorld().getPlayer().getPosX() == 1 && getWorld().getPlayer().getPosY() == 0))))
                {
                    return; //Pas de déplacement en TP en haut.
                }

                if((x == 0 && y == 12 && !((getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 11) || (getWorld().getPlayer().getPosX() == 1 && getWorld().getPlayer().getPosY() == 12))))
                {
                    return; //Pas de déplacement en bas.
                }

                if((x == 12 && y == 12 && !((getWorld().getPlayer().getPosX() == 11 && getWorld().getPlayer().getPosY() == 12) || (getWorld().getPlayer().getPosX() == 12 && getWorld().getPlayer().getPosY() == 11))))
                {
                    return; //Pas de déplacement en bas à droite.
                }


                if(validiteeDeplacement(x,y,dir))
                {
                    getWorld().getPlayer().setInAnimation(true);
                    animation(x,y,dir);
                    getWorld().getPlayer().setDir(dir);
                    setDirForMobs(getWorld().getPlayer().getNbDeplacements(), getWorld().getPlayer().getDir());
                    getWorld().getPlayer().setNbDeplacements(1);
                    getWorld().getPlayer().setMaterials(5); //on ajoute 10 matériaux par bloc cassés
                }
            }
        });


        //Lecture des touches

        this.getIhm().addKeyListener(new KeyAdapter() { //Ouverture du menu
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //Touche échap
                    if(!pauseMenu.isOpen())
                    {
                        pauseMenu.drawMenu();
                    }else{
                        //Si le menu se ferme on remet tout sur la frame principale.
                        getIhm().setMenuOpen(!getIhm().isMenuOpen());
                        pauseMenu.setMenuOpen(!pauseMenu.isOpen());
                    }
                }

                if(getIhm().isMenuOpen() || getWorld().getPlayer().isAnimation() || getWorld().isInHouse())
                {
                    return;
                }

                int x = getWorld().getPlayer().getPosX();
                int y = getWorld().getPlayer().getPosY();

                if(e.getKeyCode() == KeyEvent.VK_Z)
                {
                    if(validiteeDeplacement(x, y - 1, 'N'))
                    {
                        getWorld().getPlayer().setInAnimation(true);
                        animation(x,y - 1,'N');
                        getWorld().getPlayer().setDir('N');
                        setDirForMobs(getWorld().getPlayer().getNbDeplacements(), getWorld().getPlayer().getDir());
                        getWorld().getPlayer().setNbDeplacements(1);
                        getWorld().getPlayer().setMaterials(5); //on ajoute 10 matériaux par bloc cassés
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Q)
                {
                    if(validiteeDeplacement(x - 1, y, 'O'))
                    {
                        getWorld().getPlayer().setInAnimation(true);
                        animation(x - 1,y,'O');
                        getWorld().getPlayer().setDir('O');
                        setDirForMobs(getWorld().getPlayer().getNbDeplacements(), getWorld().getPlayer().getDir());
                        getWorld().getPlayer().setNbDeplacements(1);
                        getWorld().getPlayer().setMaterials(5); //on ajoute 10 matériaux par bloc cassés
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_S)
                {
                    if(validiteeDeplacement(x, y + 1, 'S'))
                    {
                        getWorld().getPlayer().setInAnimation(true);
                        animation(x,y + 1,'S');
                        getWorld().getPlayer().setDir('S');
                        setDirForMobs(getWorld().getPlayer().getNbDeplacements(), getWorld().getPlayer().getDir());
                        getWorld().getPlayer().setNbDeplacements(1);
                        getWorld().getPlayer().setMaterials(5); //on ajoute 10 matériaux par bloc cassés

                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D)
                {
                    if(validiteeDeplacement(x + 1, y, 'E'))
                    {
                        getWorld().getPlayer().setInAnimation(true);
                        animation(x + 1,y,'E');
                        getWorld().getPlayer().setDir('E');
                        setDirForMobs(getWorld().getPlayer().getNbDeplacements(), getWorld().getPlayer().getDir());
                        getWorld().getPlayer().setNbDeplacements(1);
                        getWorld().getPlayer().setMaterials(5); //on ajoute 10 matériaux par bloc cassés
                    }
                }
            }
        });




    }

    public Shop getShop() {
        return shop;
    }

    public Traps getTraps() {
        return traps;
    }

    public Book getBook()
    {
        return this.book;
    }

    public Runnable ClicksEvent() {
        getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if(!(e.getY() < 850 && e.getY() > 800))
                {
                    return;
                }

                if(e.getX() > 640 && e.getX() < 734)
                {
                    getBook().drawBook();
                }
                if(e.getX() > 735 && e.getX() < 800)
                {
                    getShop().drawShop();
                }
                if(e.getX() > 801 && e.getX() < 870)
                {
                    getTraps().drawTrap();
                }
            }
        });


        return null;
    }


    private boolean validiteeDeplacement(int x, int y,char dir)
    {
        int nbAngle = 0;

        if(x > 12 || x < 0 || y < 0 || y > 12)
        {
            return false;
        }

        if((x == 0 && y == 0) || (x == 0 && y == 12) || (x == 12 && y == 12))
        {
            return true;
        }

        //Pas dans les diagonales
        if (    (x > getWorld().getPlayer().getPosX() && y > getWorld().getPlayer().getPosY()) ||
                (x < getWorld().getPlayer().getPosX() && y > getWorld().getPlayer().getPosY()) ||
                (x < getWorld().getPlayer().getPosX() && y < getWorld().getPlayer().getPosY()) ||
                (x > getWorld().getPlayer().getPosX() && y < getWorld().getPlayer().getPosY())   )
        {
            return false;
        }

        if( getWorld().getType(x,y) != 0 && getWorld().getPlayer().getPosY() > 1) //On ne se déplace que sur de la terre
        {
            return false;
        }

        if(     getWorld().getPlayer().getPosX() + 1 < x || getWorld().getPlayer().getPosX() - 1 > x ||
                getWorld().getPlayer().getPosY() + 1 < y || getWorld().getPlayer().getPosY() - 1 > y   )
        {
            return false; // Pas de déplacement à plus de 1 bloc de distance
        }

        if( x == 11 && y == 3) //Si on est devant la maison on laisse rentrer
        {
            return true;
        }


        //Les coins
        if ( (getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 0) && !(dir == 'S' || dir == 'E') ) //Coin haut gauche
        {
            return false;
        }else if((getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 0)) {
            switch (dir)
            {
                case 'S' : if (getWorld().getType(x, y + 1) != 0 && getWorld().getType(x, y + 1) != 7) return false;
                case 'E' : if (getWorld().getType(x + 1, y) != 0 && getWorld().getType(x + 1, y) != 7) return false;
            }
            return true;
        }

        if ( (getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 12) && !(dir == 'N' || dir == 'E') ) //Coin bas gauche
        {
            return false;
        }else if((getWorld().getPlayer().getPosX() == 0 && getWorld().getPlayer().getPosY() == 12)) {
            switch (dir)
            {
                case 'N' : if (getWorld().getType(x, y - 1) != 0 && getWorld().getType(x, y - 1) != 7) return false;
                case 'E' : if (getWorld().getType(x + 1, y) != 0 && getWorld().getType(x + 1, y) != 7) return false;
            }
            return true;

        }

        if ( (this.getWorld().getPlayer().getPosX() == 12 && getWorld().getPlayer().getPosY() == 12) && !(dir == 'N' || dir == 'O') ) //Coin haut gauche
        {
            return false;
        }else if((getWorld().getPlayer().getPosX() == 12 && getWorld().getPlayer().getPosY() == 12)) {
            switch (dir)
            {
                case 'N' : if (getWorld().getType(x, y - 1) != 0 && getWorld().getType(x, y - 1) != 7) return false;
                case 'O' : if (getWorld().getType(x - 1, y) != 0 && getWorld().getType(x - 1, y) != 7) return false;
            }
            return true;
        }



        if (this.getWorld().getPlayer().getPosX() == 1 && this.getWorld().getPlayer().getPosY() == 12) //Anti bug bas gauche
        {
            return (dir == 'E' && this.getWorld().getType(x, y - 1) == 0);
        }

        if (this.getWorld().getPlayer().getPosX() == 1 && this.getWorld().getPlayer().getPosY() == 0) //Anti bug haut gauche
        {
            return (dir == 'E' && this.getWorld().getType(x, y + 1) == 0);
        }

        switch (x)//On regarde par rapport au x
        {
            case 0 :  if(dir == 'O')
            {
                return (getWorld().getType(x, y + 1) == 0) && (getWorld().getType(x, y - 1) == 0);
            }
            case 12 : if(dir == 'E')
            {
                return (getWorld().getType(x, y + 1) == 0) && (getWorld().getType(x, y - 1) == 0);
            }
        }

        switch (y)
        {
            case 0 :  if(dir == 'N'){return (getWorld().getType(x + 1, y) == 0) && (getWorld().getType(x - 1, y) == 0);}
            case 12 : if(dir == 'S'){return (getWorld().getType(x + 1, y) == 0) && (getWorld().getType(x - 1, y) == 0);}
        }


        switch (this.getWorld().getPlayer().getPosX())
        {
            case 0 :
            if(dir == 'N')
            {
                return (this.getWorld().getType(x, y - 1) == 0) && (this.getWorld().getType(x + 1, y) == 0) && this.getWorld().getType(x + 1, y - 1) == 0;
            }else{
                return (this.getWorld().getType(x, y + 1) == 0) && (this.getWorld().getType(x + 1, y) == 0) && this.getWorld().getType(x + 1, y + 1) == 0;
            }


            case 12 :
            if(dir == 'N')
            {
                return (this.getWorld().getType(x, y - 1) == 0) && (this.getWorld().getType(x - 1, y) == 0 && this.getWorld().getType(x - 1, y - 1) == 0);
            }else{
                return (this.getWorld().getType(x, y + 1) == 0) && (this.getWorld().getType(x - 1, y) == 0 && this.getWorld().getType(x - 1, y + 1) == 0);
            }
        }


        switch (this.getWorld().getPlayer().getPosY())
        {
            case 0 : if(dir == 'E')
            {
                return (this.getWorld().getType(x + 1, y) == 0) && (this.getWorld().getType(x, y + 1) == 0 && this.getWorld().getType(x + 1, y + 1) == 0);
            }else{
                return (this.getWorld().getType(x - 1, y) == 0) && (this.getWorld().getType(x, y + 1) == 0 && this.getWorld().getType(x - 1, y + 1) == 0);
            }

            case 12 : if(dir == 'E')
            {
                return (this.getWorld().getType(x + 1, y) == 0) && (this.getWorld().getType(x, y - 1) == 0 && this.getWorld().getType(x + 1, y - 1) == 0);
            }else{
                return (this.getWorld().getType(x - 1, y) == 0) && (this.getWorld().getType(x, y - 1) == 0 && this.getWorld().getType(x - 1, y - 1) == 0);
            }
        }


        //On regarde si on cole pas un bloc par rapport à sa direction
        switch(dir){
            case 'N' : if(this.getWorld().getType(x - 1, y) != 0 || this.getWorld().getType(x + 1, y) != 0){return false;}break;
            case 'O' : if(this.getWorld().getType(x, y - 1) != 0 || this.getWorld().getType(x, y + 1) != 0){return false;}break;
            case 'S' : if(this.getWorld().getType(x + 1, y) != 0 || this.getWorld().getType(x - 1, y) != 0){return false;}break;
            case 'E' : if(this.getWorld().getType(x, y + 1) != 0 || this.getWorld().getType(x, y - 1) != 0){return false;}break;
        }


        //On regarde les angles
        if(this.getWorld().getType(x + 1, y + 1) != 0)
        {
            nbAngle++;
        }

        if(this.getWorld().getType(x - 1, y - 1) != 0)
        {
            nbAngle++;
        }

        if(this.getWorld().getType(x - 1, y + 1) != 0)
        {
            nbAngle++;
        }

        if(this.getWorld().getType(x + 1, y - 1) != 0)
        {
            nbAngle++;
        }

        if (nbAngle >= 2) //Si il y a plus d'angles alors on est dans une diagonale
        {
            return false;
        }
        return true;
    }


    public void mobsAnimationMove()
    {
        Timer timer = new Timer(10, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for ( int i = 0 ; i < getWorld().getNbDeMobsSpawn() ; i++ )
                {
                    if( !getWorld().getTabMobs()[i].isDead() && !getWorld().getTabMobs()[i].isAnimation() ) {
                        int x = getWorld().getTabMobs()[i].getPosMobDeplacementX();
                        int y = getWorld().getTabMobs()[i].getPosMobDeplacementY();

                        if (getWorld().getTabMobs()[i].getNbDeplacement() > getWorld().getPlayer().getNbDeplacements())// on ne se déplace pas plus que le joueur
                        {
                            return;
                        }

                        getWorld().getTabMobs()[i].setDir(getDirForMobs()[getWorld().getTabMobs()[i].getNbDeplacement()]);//On change la direction selon le nb de déplacements du mob


                        switch (getWorld().getTabMobs()[i].getDir()) {
                            case 'N':
                                y -= 50;
                                break;
                            case 'O':
                                x -= 50;
                                break;
                            case 'S':
                                y += 50;
                                break;
                            case 'E':
                                x += 50;
                                break;
                        }


                        if(getWorld().getPlayer().getHp() > 0)
                        {
                            animationMobs(x, y, getWorld().getTabMobs()[i]);
                            getWorld().getTabMobs()[i].setNbDeplacement(1);//On ajoute un déplacement
                        }
                    }
                }
            }
        });
        timer.start();
    }


    public Runnable spawnMobs() //On fait spawn un mob tt les 2 secondes
    {
        getWorld().setNbMobs(getWorld().getWave() * 5);

        Timer spawn = new Timer(3000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!getPauseMenu().isOpen())//Si le jeu n'est pas en pause
                {
                    if (getWorld().getNbDeMobsSpawn() < getWorld().getWave() * 5) //Tant que le tableau de mobs n'est pas vide
                    {
                        getWorld().setNbDeMobsSpawn(1); //On ajoute 1 au total de mob qui ont spawn
                    }

                    if (getWorld().getNbMobs() == 0)
                    {
                        getWorld().setWave(1);
                        getWorld().setNbDeMobsSpawn(-getWorld().getNbDeMobsSpawn());
                        getWorld().setTime(10); //On remet le timer à 10
                        timerStart();
                        ((Timer)e.getSource()).stop();
                    }

                    if (getWorld().getPlayer().getHp() == 0)
                    {
                        ((Timer)e.getSource()).stop();
                    }
                }
            }
        });
        mobsAnimationMove(); //On fait bouger les mobs
        spawn.start();
        return null;
    }


    private Runnable animationDefenses()
    {
        Timer timerShoot = new Timer(2000, new ActionListener() //Tps pour spawn
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                if(!getPauseMenu().isOpen())//Si le jeu n'est pas en pause
                {
                    if(getWorld().getNbDeMobsSpawn() > 0 && getWorld().getNbMobs() == 0) //Si on à déja fait spawn des mobs et qu'ils sont tous morts
                    {
                        getWorld().getPlayer().setMaterials(35); //à chaque fin de vague on redonne du loot au joueur
                        ((Timer) e.getSource()).stop();
                        return;
                    }

                    if(getWorld().getPlayer().getHp() == 0)
                    {
                        tdProjectile = null;
                        animationProjectiles = null;
                        ((Timer) e.getSource()).stop();
                        return;
                    }
                    shootAnimationMove(); //On fait bouger les projectiles
                }
            }
        });
        timerShoot.start();
        return null;
    }

    private void shootAnimationMove()
    {
        for ( int i = 0 ; this.getWorld().getTabTower()[i] != null ; i++ )
        {
            for (   int j = 0 ; this.getWorld().getTabTower()[i].getDir()[j] == 'N' || this.getWorld().getTabTower()[i].getDir()[j] == 'O' ||
                    this.getWorld().getTabTower()[i].getDir()[j] == 'S' || this.getWorld().getTabTower()[i].getDir()[j] == 'E'; j++ )
            {
                if(getWorld().getTime() == 0)
                {
                    this.tdProjectile = new Thread(new Projectile(this.getWorld().getTabTower()[i].getDir()[j], this.getWorld().getTabTower()[i].getX(), this.getWorld().getTabTower()[i].getY(), this.getWorld().getTabTower()[i].getProjectileNom(), this.getWorld().getTabTower()[i]));
                    this.tdProjectile.start();
                    this.getWorld().getTabTower()[i].clearProjectile();
                }
            }
        }
    }


    private void animationMobs(int x, int y, Mobs mob)
    {
        mob.setAnimation(true);


        int delay = (int)(1000 / mob.getSpeed());

        Timer timerSpawn = new Timer(delay, new ActionListener() //Vitesse de déplacement du mob
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if( mob.getState() == 3 )//Feu
                {
                    mob.setHp(-3);
                    mob.setTpsSousEffet(1);//Add 1

                    if( mob.getTpsSousEffet() == 3 )
                    {
                        mob.setState(0);
                        mob.setTpsSousEffet(0);
                    }
                }


                if( mob.getState() == 1 ) //Si il est geler
                {
                    if(mob.getTpsSousEffet() == 1)
                    {
                        mob.setSpeed(mob.getSpeed() * 2);
                        mob.setTpsSousEffet(-mob.getTpsSousEffet());
                        mob.setState(0);
                        mob.setName(mob.getOriginalName());
                    }else{
                        mob.setTpsSousEffet(1);
                        mob.setSpeed(mob.getSpeed() / 2);
                    }
                }

                Thread moving = new Thread(new AnimationMobs(getWorld(), getIhm(), mob, x, y, infosMenu));
                moving.start();
                ((Timer) e.getSource()).stop();
            }
        });
        timerSpawn.start();
    }
}