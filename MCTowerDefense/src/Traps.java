import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Traps {

    private Overworld world;
    private Interface ihm;

    private static int catusTrapPrice = 20;

    private static int waterTrapPrice = 50;

    private static int lavaTrapPrice = 150;

    private static int TNTTrapPrice = 180;

    private boolean isOpen;

    private int selectedTrap;

    private Thread clickedZone;

    private Thread clickReader;

    private InfosMenu infosMenu;

    private int x;
    private int y;


    public Traps(Overworld world, Interface ihm, InfosMenu infosMenu) //8 = cactus, 9 = eau, 10 = lave, 11 = TNT
    {
        this.world = world;
        this.ihm = ihm;
        this.isOpen = false;
        this.selectedTrap = 0;
        this.clickReader = new Thread(ClicksEvent());
        this.clickedZone = new Thread(zoneClicked());
        this.infosMenu = infosMenu;
    }

    public Interface getIhm() {
        return ihm;
    }

    public Overworld getWorld() {
        return world;
    }


    public void drawTrap()
    {
        this.getIhm().setOnTrap(true);
        this.isOpen = true;
        this.clickReader = new Thread(this.clickReader);
        this.clickedZone = new Thread(this.clickedZone);
    }


    public int getPrice(int trap)
    {
        return switch (trap) {
            case 8 -> catusTrapPrice;
            case 9 -> waterTrapPrice;
            case 10 -> lavaTrapPrice;
            case 11 -> TNTTrapPrice;
            default -> 0;
        };
    }

    public void setSelectedTrap(int selectedTrap)
    {
        this.selectedTrap = selectedTrap; // (8 = cactus, 9 = water, 10 = lava, 11 = TNT)
    }

    public Runnable ClicksEvent()
    {

        getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(!isOpenned())
                {
                    return;
                }

                if(!(e.getY() > 731 && e.getY() < 800) || clickReader.isInterrupted()) //Si on sort de la zone de click
                {
                    return;
                }



                if(e.getX() > 259 && e.getX() < 311)
                {
                    setSelectedTrap(8);
                    Cursor snowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/Cactus.png")).getImage() , new Point(0, 0), "Curseur Cactus");
                    getIhm().setCursor(snowTrapCursor);
                }
                if(e.getX() > 355 && e.getX() < 408)
                {
                    setSelectedTrap(9);
                    Cursor snowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/Water.jpeg")).getImage() , new Point(0, 0), "Curseur Water");
                    getIhm().setCursor(snowTrapCursor);
                }
                if(e.getX() > 450 && e.getX() < 504)
                {
                    setSelectedTrap(10);
                    Cursor snowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/Lava.png")).getImage() , new Point(0, 0), "Curseur Lava");
                    getIhm().setCursor(snowTrapCursor);
                }
                if(e.getX() > 545 && e.getX() < 600)
                {
                    setSelectedTrap(11);
                    Cursor snowTrapCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/TNT.png")).getImage() , new Point(0, 0), "Curseur TNT");
                    getIhm().setCursor(snowTrapCursor);
                }
                if(e.getX() > 632 && e.getX() < 742)
                {
                    closeTraps();
                }
            }
        });


        return null;
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


                if(getWorld().getType(x, y) != 1 || !isOpenned() || getWorld().getPlayer().getMaterials() < getPrice(getSelectedTrap()))
                {
                    return;
                }

                getWorld().getPlayer().setMaterials(-getPrice(getSelectedTrap()));
                getWorld().setMap(x, y, getSelectedTrap());
                getWorld().addTraps(new Trap(getWorld(), x, y + 1, getSelectedTrap()));
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

    private void closeTraps()
    {
        this.getIhm().setOnTrap(false);
        Cursor pathCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/overworld/cursorPath.png")).getImage() , new Point(0, 0), "Curseur path");
        getIhm().setCursor(pathCursor);
        this.clickReader = null;
        this.clickedZone = null;
        this.isOpen = false;
    }
}
