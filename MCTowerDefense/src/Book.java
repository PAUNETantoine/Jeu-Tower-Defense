import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Book {

    private Overworld world;

    private Interface ihm;

    private boolean isOpen;

    private Thread clickReader;

    private InfosMenu infosMenu;

    public Book(Overworld world, Interface ihm, InfosMenu infosMenu)
    {
        this.world = world;
        this.ihm = ihm;
        this.isOpen = true;
        this.clickReader = new Thread(ClicksEvent());
        this.clickReader.interrupt();
        this.infosMenu = infosMenu;

    }

    public void drawBook()
    {
        this.getIhm().setOnBook(true);
        this.clickReader = new Thread(this.clickReader);
    }

    public void setSelectedTrap(int selectedTrap)
    {
        //this.selectedTrap = selectedTrap; // (2 = snowBall, 5 = egg, 6 = arrow, 7 = fireBall)
    }

    public Runnable ClicksEvent() {
        getIhm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if(!(e.getY() > 731 && e.getY() < 800) || clickReader.isInterrupted()) //Si on sort de la zone de click
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
                    closeBook();
                }
            }
        });


        return null;
    }

    private void closeBook()
    {
        this.getIhm().setOnBook(false);
        this.clickReader.interrupt();
    }

    public Interface getIhm()
    {
        return ihm;
    }

    public Overworld getWorld()
    {
        return world;
    }
}
