import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Shop {

    private Overworld world;

    private Interface ihm;
    
    private boolean isOpen;

    private int selectedTrap;

    private Thread clickReader;

    private Thread clickedZone;

    private final int snowTrapPrice = 25;
    private final int eggTrapPrice = 35;
    private final int arrowTrapPrice = 50;
    private final int fireBallTrapPrice = 70;

    private InfosMenu infosMenu;

    public Shop(Overworld world, Interface ihm, InfosMenu infosMenu)
    {
        this.world = world;
        this.ihm = ihm;
        this.isOpen = false;
        this.selectedTrap = 0;
        this.clickReader = new Thread(ClicksEvent());
        this.clickedZone = new Thread(zoneClicked());
        this.clickReader.interrupt();
        this.clickedZone.interrupt();
        this.infosMenu = infosMenu;
    }

    public Overworld getWorld()
    {
        return world;
    }

    public Interface getIhm()
    {
        return ihm;
    }

    public int getPrice(int trap)
    {
        return switch (trap) {
            case 2 -> snowTrapPrice;
            case 5 -> eggTrapPrice;
            case 6 -> arrowTrapPrice;
            case 7 -> fireBallTrapPrice;
            default -> 0;
        };
    }


    public void drawShop()
    {
        this.isOpen = true;
        this.getIhm().setOnShop(true);
        this.clickReader = new Thread(this.clickReader);
        this.clickedZone = new Thread(this.clickedZone);
    }

    public void setSelectedTrap(int selectedTrap)
    {
        this.selectedTrap = selectedTrap; // (2 = snowBall, 5 = egg, 6 = arrow, 7 = fireBall)
    }

    public Runnable ClicksEvent() {

        getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(!isOpenned())
                {
                    return;
                }


                if(!(e.getY() > 731 && e.getY() < 800)) //Si on sort de la zone de click
                {
                    return;
                }



                if(e.getX() > 259 && e.getX() < 311)
                {
                    setSelectedTrap(2);
                    Cursor snowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/snowBallTrap.png")).getImage() , new Point(0, 0), "Curseur SnowTrap");
                    getIhm().setCursor(snowTrapCursor);
                }
                if(e.getX() > 355 && e.getX() < 408)
                {
                    setSelectedTrap(5);
                    Cursor eggTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/eggTrap.png")).getImage() , new Point(0, 0), "Curseur eggTrap");
                    getIhm().setCursor(eggTrapCursor);
                }
                if(e.getX() > 450 && e.getX() < 504)
                {
                    setSelectedTrap(6);
                    Cursor arrowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/arrowTrap.png")).getImage() , new Point(0, 0), "Curseur arrowTrap");
                    getIhm().setCursor(arrowTrapCursor);
                }
                if(e.getX() > 545 && e.getX() < 600)
                {
                    setSelectedTrap(7);
                    Cursor fireBallTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/fireBallTrap.png")).getImage() , new Point(0, 0), "Curseur fireBallTrap");
                    getIhm().setCursor(fireBallTrapCursor);
                }
                if(e.getX() > 632 && e.getX() < 742)
                {
                    String cursorName;

                    if (getWorld().isInHouse())
                    {
                        cursorName = "wood";
                    }else{
                        cursorName = "cursorPath";
                    }

                    Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/"+ cursorName +".png")).getImage() , new Point(0, 0), "Curseur fireBallTrap");
                    getIhm().setCursor(cursor);
                    closeShop();
                }
            }
        });


        return null;
    }

    public String getNomTrap(int type)
    {
        switch(type)
        {
            case 2 : return "snowTrap";
            case 6 : return "eggTrap";
            case 7 : return "arrowTrap";
            case 8 : return "fireBallTrap";
        }
        return "";
    }

    public char[] getDirs(int x, int y)
    {
        char[] dirs = new char[4];
        int cpt = 0;


        if(x == 12 && y == 12) //Coin bas droite
        {
            if(getWorld().getType(x, y-1) == 1 )
            {
                dirs[cpt] = 'N';
                cpt++;
            }

            if(getWorld().getType(x-1, y) == 1)
            {
                dirs[cpt] = 'O';
            }
            return dirs;
        }


        if(x == 0 && y == 12) //Coin bas gauche
        {
            if(getWorld().getType(x, y-1) == 1 )
            {
                dirs[cpt] = 'N';
                cpt++;
            }

            if(getWorld().getType(x+1, y) == 1)
            {
                dirs[cpt] = 'E';
            }
            return dirs;
        }

        if(x == 0 && y == 0) //Coin bas droite
        {
            if(getWorld().getType(x, y+1) == 1 )
            {
                dirs[cpt] = 'S';
                cpt++;
            }

            if(getWorld().getType(x+1, y) == 1)
            {
                dirs[cpt] = 'E';
            }
            return dirs;
        }



        if(x > 11)
        {
            if((getWorld().getType(x-1, y) == 1))
            {
                dirs[cpt] = 'O';
                cpt++;
            }
            if((getWorld().getType(x, y-1) == 1))
            {
                dirs[cpt] = 'N';
                cpt++;
            }
            if((getWorld().getType(x, y+1) == 1))
            {
                dirs[cpt] = 'S';
            }
            return dirs;
        }

        if(x < 1)
        {
            if((getWorld().getType(x+1, y) == 1))
            {
                dirs[cpt] = 'E';
                cpt++;
            }
            if((getWorld().getType(x, y-1) == 1))
            {
                dirs[cpt] = 'N';
                cpt++;
            }
            if((getWorld().getType(x, y+1) == 1))
            {
                dirs[cpt] = 'S';
            }
            return dirs;
        }

        if(y > 11)
        {
            if((getWorld().getType(x-1, y) == 1))
            {
                dirs[cpt] = 'O';
                cpt++;
            }
            if((getWorld().getType(x, y-1) == 1))
            {
                dirs[cpt] = 'N';
                cpt++;
            }
            if((getWorld().getType(x+1, y) == 1))
            {
                dirs[cpt] = 'E';
            }
            return dirs;
        }

        if(y < 1)
        {
            if((getWorld().getType(x-1, y) == 1))
            {
                dirs[cpt] = 'O';
                cpt++;
            }
            if((getWorld().getType(x, y+1) == 1))
            {
                dirs[cpt] = 'S';
                cpt++;
            }
            if((getWorld().getType(x, y+1) == 1))
            {
                dirs[cpt] = 'S';
            }
            return dirs;
        }

        //Si autre part que sur les bords

        if((getWorld().getType(x-1, y) == 1))
        {
            dirs[cpt] = 'O';
            cpt++;
        }
        if((getWorld().getType(x, y-1) == 1))
        {
            dirs[cpt] = 'N';
            cpt++;
        }
        if((getWorld().getType(x, y+1) == 1))
        {
            dirs[cpt] = 'S';
            cpt++;
        }
        if((getWorld().getType(x+1, y) == 1))
        {
            dirs[cpt] = 'E';
        }

        return dirs;
    }


    public Runnable zoneClicked()
    {
        getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {

                if(!(e.getX() > 50 && e.getX() < 700) || !(e.getY() > 80 && e.getY() < 730))
                {
                    return;
                }

                int x = (e.getX()-50) / 50;
                int y = (e.getY() - 80) / 50;


                if(getWorld().getType(x, y) != 0 || !isOpenned())
                {
                    return;
                }


                if(x == 0 && y == 0) //Coin haut gauche
                {
                    if(getWorld().getType(x, y+1) == 1 || getWorld().getType(x+1, y) == 1)
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }


                if(x == 0 && y == 12) //Coin bas gauche
                {
                    if(getWorld().getType(x, y-1) == 1 || getWorld().getType(x+1, y) == 1)
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }

                if(x == 12 && y == 12) //Coin bas droite
                {
                    if(getWorld().getType(x, y-1) == 1 || getWorld().getType(x-1, y) == 1)
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }


                if(x > 11)
                {
                    if((getWorld().getType(x-1, y) == 1 || getWorld().getType(x, y+1) == 1 || getWorld().getType(x, y-1) == 1) && getWorld().getPlayer().getMaterials() >= getPrice(getSelectedTrap()))
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }

                if(x < 1)
                {
                    if((getWorld().getType(x+1, y) == 1 || getWorld().getType(x, y+1) == 1 || getWorld().getType(x, y-1) == 1)  && getWorld().getPlayer().getMaterials() >= getPrice(getSelectedTrap()))
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }

                if(y > 11)
                {
                    if((getWorld().getType(x-1, y) == 1 || getWorld().getType(x+1, y) == 1 || getWorld().getType(x, y-1) == 1)  && getWorld().getPlayer().getMaterials() >= getPrice(getSelectedTrap()))
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }

                if(y < 1)
                {
                    if((getWorld().getType(x-1, 1) == 1 || getWorld().getType(x+1, y) == 1 || getWorld().getType(x, y+1) == 1) && getWorld().getPlayer().getMaterials() >= getPrice(getSelectedTrap()))
                    {
                        getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                        getWorld().setMap(x, y, getSelectedTrap());
                        getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                        return;
                    }
                }

                if((getWorld().getType(x+1, y) == 1 || getWorld().getType(x-1, y) == 1 || getWorld().getType(x, y+1) == 1 || getWorld().getType(x, y-1) == 1) && getWorld().getPlayer().getMaterials() >= getPrice(getSelectedTrap()))
                {
                    getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                    getWorld().setMap(x, y, getSelectedTrap());
                    getWorld().addTower(new Tower(getSelectedTrap(), x * 50 + 50, y * 50 + 80, getNomTrap(getSelectedTrap()), 1, getDirs(x,y), getWorld(), getIhm()));
                }

            }
            });

        return null;
    }

    public boolean isOpenned()
    {
        return isOpen;
    }

    private int getSelectedTrap()
    {
        return this.selectedTrap;
    }

    private void closeShop()
    {
        this.getIhm().setOnShop(false);
        this.clickReader = null;
        this.clickedZone = null;
        this.isOpen = false;
    }
}
