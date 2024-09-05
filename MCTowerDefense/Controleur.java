package old;

import javax.swing.*;
import java.awt.event.*;

public class Controleur {

    private Overworld world;

    private Interface ihm;

    private PauseMenu pauseMenu;

    private InfosMenu infosMenu;

    private boolean isAnimation;

    private char[] dirForMobs;

    public Controleur(Overworld world, Interface ihm)
    {
        this.world = world;
        this.ihm = ihm;
        this.pauseMenu = new PauseMenu(this.getIhm(), this.getWorld());
        this.infosMenu = new InfosMenu(this.getIhm(), this.getWorld());
        this.isAnimation = false;
        this.dirForMobs = new char[50];
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

    public boolean isAnimation()
    {
        return isAnimation;
    }

    public void setInAnimation(boolean b)
    {
        this.isAnimation = b;
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


                //On empêche plusieurs executions en même temps
                setInAnimation(true);




                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;



                if (elapsedTime >= animationDuration) {
                    // Arrêter le timer une fois que l'animation est terminée et repositionner les éléments correctement
                    getIhm().reloadFinal(getWorld(), dir ,getWorld().getPlayer().getPosX(), getWorld().getPlayer().getPosY());
                    getInfosMenu().drawInfos();
                    setInAnimation(false);
                    ((Timer) e.getSource()).stop();
                    return;
                }


                // Calculer la position intermédiaire en fonction du temps écoulé
                float progress = (float) elapsedTime / animationDuration;
                int xFinal = (int) (xDep + progress * (xArr - xDep));
                int yFinal = (int) (yDep + progress * (yArr - yDep));


                //On recharge l'IHM à chaques fois qu'on fait une animation
                if(getIhm().isAllMobsDrawed()) //Si tout les mobs sont bien affichés
                {
                    getIhm().reload(getWorld(), dir, xFinal ,yFinal);
                    getInfosMenu().drawInfos();
                }

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
            getIhm().reload(getWorld(), dir, getWorld().getPlayer().getPosX() , getWorld().getPlayer().getPosY());

            getIhm().drawBlock("house_2.png", 10, 0); //On met le joueur dans la maison
        }
    }


    public char[] getDirForMobs()
    {
        return dirForMobs;
    }

    public void setDirForMobs(char[] tab)
    {
        for (int i = 0; i < tab.length ; i++)
        {
            dirForMobs[i] = tab[i];
        }
    }

    public void timerStart()
    {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!getPauseMenu().isOpen())//Si le jeu n'est pas en pause
                {
                    if(getWorld().getTime() > 0)
                    {
                        getWorld().setTime(-1);
                    }else{
                        getWorld().initMobs( getWorld().getWave() ); //Génération du nombre de mobs
                        spawnMobs();//Quand le timer est à 0 on lance la vague
                        ((Timer) e.getSource()).stop();
                    }
                    getIhm().reloadFinal(getWorld(), getWorld().getPlayer().getDir(), getWorld().getPlayer().getPosX(), getWorld().getPlayer().getPosY());
                    getInfosMenu().drawInfos();
                }
            }
        });

        // Démarrer le chronomètre
        timer.start();
    }

    public PauseMenu getPauseMenu()
    {
        return pauseMenu;
    }

    public void setDirForMobs(int id, char dir)
    {
        this.dirForMobs[id] = dir;
    }


    private void start()
    {
        this.getIhm().setMapIHM(this.getWorld().getMap()); //On génère la map graphique
        this.getIhm().drawPlayer(this.getWorld().getPlayer().getPosX(), this.getWorld().getPlayer().getPosY(), this.getWorld().getPlayer().getDir()); //On pose le joueur
        this.getInfosMenu().drawInfos();

        timerStart();

        //Quand on clic on recupère les coordonnées
        this.getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getIhm().isMenuOpen() || isAnimation() || getWorld().isInHouse())
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


                if(validiteeDeplacement(x,y,dir))
                {
                    animation(x,y,dir);
                    getWorld().getPlayer().setDir(dir);
                    if(getWorld().getPlayer().getMaterials() != 0)
                    {
                        setDirForMobs(getWorld().getPlayer().getMaterials()/10, getWorld().getPlayer().getDir());
                    }
                    getWorld().getPlayer().setMaterials(10); //on ajoute 10 matériaux par bloc cassés
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
                        getIhm().reload(getWorld(),getWorld().getPlayer().getDir(),getWorld().getPlayer().getPosX(),getWorld().getPlayer().getPosY());
                        getIhm().setMenuOpen(!getIhm().isMenuOpen());
                        pauseMenu.setMenuOpen(!pauseMenu.isOpen());
                        getInfosMenu().drawInfos();
                    }
                }

                if(getIhm().isMenuOpen() || isAnimation() || getWorld().isInHouse())
                {
                    return;
                }

                int x = getWorld().getPlayer().getPosX();
                int y = getWorld().getPlayer().getPosY();

                if(e.getKeyCode() == KeyEvent.VK_Z)
                {
                    if(validiteeDeplacement(x, y - 1, 'N'))
                    {
                        animation(x,y - 1,'N');
                        getWorld().getPlayer().setDir('N');
                        if(getWorld().getPlayer().getMaterials() != 0)
                        {
                            setDirForMobs(getWorld().getPlayer().getMaterials()/10, getWorld().getPlayer().getDir());
                        }
                        getWorld().getPlayer().setMaterials(10); //on ajoute 10 matériaux par bloc cassés
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Q)
                {
                    if(validiteeDeplacement(x - 1, y, 'O'))
                    {
                        animation(x - 1,y,'O');
                        getWorld().getPlayer().setDir('O');
                        if(getWorld().getPlayer().getMaterials() != 0)
                        {
                            setDirForMobs(getWorld().getPlayer().getMaterials()/10, getWorld().getPlayer().getDir());
                        }
                        getWorld().getPlayer().setMaterials(10); //on ajoute 10 matériaux par bloc cassés
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_S)
                {
                    if(validiteeDeplacement(x, y + 1, 'S'))
                    {
                        animation(x,y + 1,'S');
                        getWorld().getPlayer().setDir('S');
                        if(getWorld().getPlayer().getMaterials() != 0)
                        {
                            setDirForMobs(getWorld().getPlayer().getMaterials()/10, getWorld().getPlayer().getDir());
                        }
                        getWorld().getPlayer().setMaterials(10); //on ajoute 10 matériaux par bloc cassés

                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D)
                {
                    if(validiteeDeplacement(x + 1, y, 'E'))
                    {
                        animation(x + 1,y,'E');
                        getWorld().getPlayer().setDir('E');
                        if(getWorld().getPlayer().getMaterials() != 0)
                        {
                            setDirForMobs(getWorld().getPlayer().getMaterials()/10, getWorld().getPlayer().getDir());
                        }
                        getWorld().getPlayer().setMaterials(10); //on ajoute 10 matériaux par bloc cassés
                    }
                }

            }
        });




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
        for ( int i = 0 ; i < this.getWorld().getNbDeMobsSpawn() ; i++ )
        {
            if( !this.getWorld().getTabMobs()[i].isDead() )
            {
                int x = this.getWorld().getTabMobs()[i].getPosMobDeplacementX();
                int y = this.getWorld().getTabMobs()[i].getPosMobDeplacementY();


                this.getWorld().getTabMobs()[i].setDir(this.getDirForMobs()[this.getWorld().getTabMobs()[i].getNbDeplacement()]);//On change la direction selon le nb de déplacements du mob

                switch(this.getWorld().getTabMobs()[i].getDir())
                {
                    case 'N' : y -= 50; break;
                    case 'O' : x -= 50; break;
                    case 'S' : y += 50; break;
                    case 'E' : x += 50; break;
                }

                animationMobs(x, y, this.getWorld().getTabMobs()[i].getDir(), this.getWorld().getTabMobs()[i]);
                this.getWorld().getTabMobs()[i].setNbDeplacement(1);//On ajoute un déplacement
            }
        }
    }


    public void spawnMobs() //On fait spawn un mob tt les 2 secondes
    {
        Timer timerSpawn = new Timer(2000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!getPauseMenu().isOpen())//Si le jeu n'est pas en pause
                {
                    if(getWorld().getNbDeMobsSpawn() < getWorld().getWave()) //Tant que le tableau de mobs n'est pas vide
                    {
                        getWorld().setNbMobs(1);
                        getWorld().setNbDeMobsSpawn(1); //On ajoute 1 au total de mob qui ont spawn
                    }

                    mobsAnimationMove(); //On fait bouger les mobs

                    if(getWorld().getNbMobs() == 0 || getWorld().getPlayer().getHp() <= 0)
                    {
                        getWorld().setNbDeMobsSpawn(-getWorld().getNbDeMobsSpawn());
                        getWorld().setTime(60); //On remet le timer à 60
                        ((Timer) e.getSource()).stop();
                    }
                }
            }
        });
        timerSpawn.start();
    }



    private void animationMobs(int x, int y, char dir, Mobs mob)
    {

        // Définir les points de départ et d'arrivée
        int xDep = mob.getPosMobDeplacementX();
        int yDep = mob.getPosMobDeplacementY();
        int xArr = x;
        int yArr = y;




        // Durée de l'animation en millisecondes
        int animationDuration = (int) (2000 / mob.getSpeed()); //Vitesse selont la vitesse mob

        Thread moving = new Thread(new AnimationMobs(getWorld(), getIhm(), mob, x, y));

        Timer timer = new Timer(60, new ActionListener()
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
                    mob.setPosMobDeplacementX(x);
                    mob.setPosMobDeplacementY(y);

                    getInfosMenu().drawInfos();
                    ((Timer) e.getSource()).stop();
                    return;
                }


                if ( mob.isDead() )
                {
                    getWorld().setNbMobs(-1);
                    getIhm().reloadMobs(getWorld(), mob.getDir(), mob);
                    getInfosMenu().drawInfos();
                    ((Timer) e.getSource()).stop();
                    return;
                }


                if( mob.dealDmgToPlayer( getWorld().getPlayer() ) )
                {
                    getWorld().getPlayer().setHp(-1);
                }



                // Calculer la position intermédiaire en fonction du temps écoulé
                float progress = (float) elapsedTime / animationDuration;
                int xFinal = (int) (xDep + progress * (xArr - xDep));
                int yFinal = (int) (yDep + progress * (yArr - yDep));

                System.out.println(xFinal + " " + yFinal);



                //On recharge l'IHM à chaques fois qu'on fait une animation
                if(mob.getDir() == 'O' || mob.getDir() == 'E')
                {
                    mob.setPosMobDeplacementX(xFinal);
                }else{
                    mob.setPosMobDeplacementY(yFinal);
                }
                getIhm().reloadMobs(getWorld(), mob.getDir(), mob);
                getInfosMenu().drawInfos();
            }
        });

        // Démarrer le timer
        timer.start();
        mob.setPosMobDeplacementY(y);
        mob.setPosMobDeplacementX(x);
    }
}