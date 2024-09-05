import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Interface extends JFrame{


    private int width;
    private int height;

    private JPanel panel;
    private JLayeredPane mapPanel;

    private boolean allMobsDrawed;

    private boolean onShop;
    private boolean onBook;
    private boolean onTrap;

    private boolean menuOpen;

    public boolean isAllMobsDrawed() {
        return allMobsDrawed;
    }

    public void setAllMobsDrawed(boolean allMobsDrawed)
    {
        this.allMobsDrawed = allMobsDrawed;
    }

    public Interface(String title ) //Constructeur de notre Interface de jeu
    {
        //Mise en place du fond d'écran
        this.setTitle( title );
        this.setSize( 1920 , 1080 );
        this.panel = new JPanel();
        this.mapPanel = new JLayeredPane();
        this.add(this.mapPanel);
        this.setLocationRelativeTo(null);
        this.setContentPane(this.mapPanel);
        this.menuOpen = false;
        this.allMobsDrawed = true;
        this.onShop = false;
        this.onBook = false;
        this.onTrap = false;
        this.setIconImage(new ImageIcon(getClass().getResource("/images/menu/logo.png")).getImage());

        this.imgBG();

        this.setVisible( true );
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cursor pathCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/cursorPath.png")).getImage() , new Point(0, 0), "Curseur path");
        this.setCursor(pathCursor);
    }

    public boolean isOnBook() {
        return onBook;
    }

    public boolean isOnShop() {
        return onShop;
    }

    public boolean isOnTrap() {
        return onTrap;
    }

    public void setOnBook(boolean onBook) {
        this.onBook = onBook;
    }

    public void setOnShop(boolean onShop) {
        this.onShop = onShop;
    }

    public void setOnTrap(boolean onTrap) {
        this.onTrap = onTrap;
    }

    public void imgBG()
    {
        //Mise en place du fond d'écran
        String bgType;

        if(this.isOnBook())
        {
            bgType = "Book";
        } else if (this.isOnShop())
        {
            bgType = "Shop";
        } else if (this.isOnTrap())
        {
            bgType = "Traps";
        }else
        {
            bgType = "Game";
        }

        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/menu/background"+ bgType +".png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        mapPanel.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
    }


    public JLayeredPane getMapPanel()
    {
        return mapPanel;
    }


    public void drawPlayerAnimation(int x, int y, char dir)
    {
        ImageIcon imgPlayer = new ImageIcon(getClass().getResource("/images/overworld/player" + dir + (int) Math.round(Math.random() + 1) + ".png"));
        JLabel blockLabel = new JLabel(imgPlayer);
        blockLabel.setBounds(x, y, imgPlayer.getIconWidth(), imgPlayer.getIconHeight());
        this.mapPanel.add(blockLabel, JLayeredPane.POPUP_LAYER);
    }

    public void drawMobAnimation(Mobs[] mob, int nbMobs)
    {
        for ( int i = 0 ; i < nbMobs ; i++)
        {
            if( !mob[i].isDead() )
            {
                ImageIcon imgMob = new ImageIcon(getClass().getResource("/images/overworld/" + mob[i].getName() + mob[i].getDir() + ".png"));
                JLabel blockLabel = new JLabel(imgMob);
                blockLabel.setBounds(mob[i].getPosMobDeplacementX() + 50, mob[i].getPosMobDeplacementY(), imgMob.getIconWidth(), imgMob.getIconHeight());
                this.mapPanel.add(blockLabel, JLayeredPane.POPUP_LAYER);
            }
        }
    }


    public void gameOver(Overworld world)
    {
        ImageIcon bgDead = new ImageIcon(getClass().getResource("/images/overworld/gameOver.png"));

        JLabel bgDeadLabel = new JLabel(bgDead);
        JLabel bgTextDead = new JLabel("Game Over !");
        JLabel bgTextScore = new JLabel("Score : " + world.getWave() + " Waves | " + world.getPlayer().getKills() + " Kills.");
        JLabel btnLabel = new JLabel("Leave Game");
        JButton btnQuit = new JButton();
        btnQuit.setIcon(new ImageIcon(getClass().getResource("/images/overworld/button.png")));
        bgTextDead.setFont(new Font("Minecraft", Font.BOLD, 60));
        bgTextScore.setFont(new Font("Minecraft", Font.BOLD, 40));
        btnLabel.setFont(new Font("Minecraft", Font.PLAIN, 30));


        btnQuit.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  SwingUtilities.invokeLater(() -> {
                      System.exit(0);
                  });
              }
        });

        bgDeadLabel.setBounds(0,0,this.getWidth(),this.getHeight());
        bgTextDead.setBounds(550, 300, 400, 60);
        bgTextScore.setBounds(510, 450, 600, 50);
        btnQuit.setBounds(630, 520, 251, 73);
        btnLabel.setBounds(660,535, 300, 50);

        this.mapPanel.add(bgDeadLabel, Integer.valueOf(1000));
        this.mapPanel.add(bgTextDead, Integer.valueOf(1100));
        this.mapPanel.add(bgTextScore, Integer.valueOf(1100));
        this.mapPanel.add(btnQuit, Integer.valueOf(1100));
        this.mapPanel.add(btnLabel, Integer.valueOf(1200));
    }



    public void setMenuOpen(boolean b)
    {
        this.menuOpen = b;
    }


    public boolean isMenuOpen()
    {
        return this.menuOpen;
    }


    public void drawPlayer(int x, int y, char dir)
    {
        ImageIcon imgPlayer = new ImageIcon(getClass().getResource("/images/overworld/player" + dir + ".png"));
        JLabel blockLabel = new JLabel(imgPlayer);
        blockLabel.setBounds(x*50 + 50, y*50 + 50, imgPlayer.getIconWidth(), imgPlayer.getIconHeight());
        this.mapPanel.add(blockLabel, JLayeredPane.POPUP_LAYER);
    }


    public void drawBlock(String type, int x, int y) //Cette méthode permet de dessiner des blocs sur le terrain
    {
        ImageIcon imgBlock = new ImageIcon(getClass().getResource("/images/overworld/" + type));
        JLabel blockLabel = new JLabel(imgBlock);
        blockLabel.setBounds(x*50 + 50, y*50 + 50, imgBlock.getIconWidth(), imgBlock.getIconHeight());

        if(type.equals("house_1.png") || type.equals("house_2.png"))//Si on doit dessiner la maison on le fait au dessus du sol
        {
            blockLabel.setBounds(x*50 + 65, y*50 + 50, imgBlock.getIconWidth(), imgBlock.getIconHeight());
            if ( type.equals("house_2.png") )
            {
                this.mapPanel.add(blockLabel, JLayeredPane.DRAG_LAYER); //On place l'image de la maison avec le joueur par dessus l'autre
            }else{
                this.mapPanel.add(blockLabel, JLayeredPane.POPUP_LAYER);
            }
            return;
        }
        this.mapPanel.add(blockLabel, JLayeredPane.MODAL_LAYER);
    }


    public void reload(Overworld world)
    {
        if(menuOpen)
        {
            return;
        }


        this.getContentPane().removeAll(); //On vide la frame et on la replace après
        this.imgBG();
        this.setMapIHM(world.getMap());

        if(world.getPlayer().getHp() == 0)
        {
            gameOver(world);
        }

        if ( !world.isInHouse() && !this.isMenuOpen()) //Si le joueur n'est pas encore dans la maison on le dessine
        {
            if(world.getPlayer().isAnimation())//Si on fait l'animation alors on met le mode animé
            {
                this.drawPlayerAnimation(world.getPlayer().getPosXDeplacement(), world.getPlayer().getPosYDeplacement(), world.getPlayer().getDir());
            }else{
                this.drawPlayer(world.getPlayer().getPosX(), world.getPlayer().getPosY(), world.getPlayer().getDir());
            }
        }else {
            this.drawBlock("house_2.png", 10, 0);
        }

        this.drawMobAnimation(world.getTabMobs(), world.getNbDeMobsSpawn());

        for(int i = 0 ; world.getTabTower()[i] != null ; i++)
        {
            this.drawProjectile(world.getTabTower()[i]);
        }
    }


    public void drawProjectile(Tower tower)
    {
        for( int i = 0 ; i < tower.getTabProjectile().length ; i++)
        {
            if(tower.getTabProjectile()[i] != null && !tower.getTabProjectile()[i].isRestart())
            {
                ImageIcon imgBlock = new ImageIcon(getClass().getResource("/images/overworld/" + tower.getTabProjectile()[i].getType() + ".png"));
                JLabel blockLabel = new JLabel(imgBlock);
                blockLabel.setBounds(tower.getTabProjectile()[i].getX() , tower.getTabProjectile()[i].getY(), imgBlock.getIconWidth(), imgBlock.getIconHeight());
                this.mapPanel.add(blockLabel, Integer.valueOf(1000));
            }
        }
    }



    public void setMapIHM(int[][] map) // Cette méthode permet de lire un tableau MAP afin de les dessiner sur le terrain
    {
        ImageIcon imgBlock = new ImageIcon(getClass().getResource("/images/overworld/mapBG.png"));
        JLabel blockLabel = new JLabel(imgBlock);
        blockLabel.setBounds(50, 50, imgBlock.getIconWidth(), imgBlock.getIconHeight());
        this.mapPanel.add(blockLabel, JLayeredPane.PALETTE_LAYER);

        for ( int i = 0 ; i < map.length ; i++ )
        {
            for ( int j = 0 ; j < map[i].length ; j++ )
            {
                switch( map[i][j] )
                {
                    case 0 : {
                        break;
                    }

                    case 1 : {
                        this.drawBlock("sand.png", j, i);
                        break;
                    }

                    case 2 : {
                        this.drawBlock("snowTrapTexture.png", j, i);
                        break;
                    }

                    case 3 : {
                        this.drawBlock("house_1.png", 10, 0);
                        break;
                    }

                    case 5 : {
                        this.drawBlock("eggTrapTexture.png", j, i);
                        break;
                    }

                    case 6 : {
                        this.drawBlock("arrowTrapTexture.png", j, i);
                        break;
                    }

                    case 7 : {
                        this.drawBlock("fireBallTrapTexture.png", j, i);
                        break;
                    }

                    case 8 : {
                        this.drawBlock("cactusTexture.png", j, i);
                        break;
                    }

                    case 9 : {
                        this.drawBlock("Water.jpeg", j, i);
                        break;
                    }

                    case 10 : {
                        this.drawBlock("Lava.png", j, i);
                        break;
                    }

                    case 11 : {
                        this.drawBlock("TNTTexture.png", j, i);
                        break;
                    }

                    default : break;
                }

            }
        }
    }



}
