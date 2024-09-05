import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseMenu {

    private Interface ihm;

    private Overworld world;

    private boolean isOpen;


    public PauseMenu(Interface ihm, Overworld world)
    {
        this.ihm = ihm;
        this.world = world;
        this.isOpen = false;
    }

    public Overworld getWorld() {
        return world;
    }



    public Interface getFrame()
    {
        return this.ihm;
    }

    public boolean isOpen()
    {
        return this.isOpen;
    }

    public void setMenuOpen(boolean b)
    {
        this.isOpen = b;
    }

    public void drawMenu()
    {
        JLabel pauseMenu = new JLabel();
        ImageIcon imgMenu = new ImageIcon(getClass().getResource("/images/overworld/pause.jpg"));
        pauseMenu.setIcon(imgMenu);

        ImageIcon btnImgBg = new ImageIcon(getClass().getResource("/images/overworld/button.png"));


        JButton returnToGame = new JButton();
        JLabel textBtn = new JLabel("      Return to Game ");
        textBtn.setFont(new Font("Minecraft", Font.PLAIN, 20));
        textBtn.setForeground(Color.WHITE);
        returnToGame.setIcon(btnImgBg);
        returnToGame.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2, 200, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2,205, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        this.getFrame().getMapPanel().add(returnToGame, Integer.valueOf(900));
        this.getFrame().getMapPanel().add(textBtn, Integer.valueOf(1000));


        JButton buttonOptions = new JButton();
        JLabel textBtn2 = new JLabel("            Options ");
        textBtn2.setFont(new Font("Minecraft", Font.PLAIN, 20));
        textBtn2.setForeground(Color.WHITE);
        buttonOptions.setIcon(btnImgBg);
        buttonOptions.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2, 300, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn2.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2,305, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        this.getFrame().getMapPanel().add(buttonOptions, Integer.valueOf(900));
        this.getFrame().getMapPanel().add(textBtn2, Integer.valueOf(1000));

        JButton quitGame = new JButton();
        JLabel textBtn3 = new JLabel("      Leave the Game ");
        textBtn3.setFont(new Font("Minecraft", Font.PLAIN, 20));
        textBtn3.setForeground(Color.WHITE);
        quitGame.setIcon(btnImgBg);
        quitGame.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2, 400, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn3.setBounds(this.getFrame().getWidth() / 2 - btnImgBg.getIconWidth() / 2,405, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        this.getFrame().getMapPanel().add(quitGame, Integer.valueOf(900));
        this.getFrame().getMapPanel().add(textBtn3, Integer.valueOf(1000));


        this.getFrame().getMapPanel().add(pauseMenu, Integer.valueOf(900));
        pauseMenu.setBounds(this.getFrame().getWidth() / 2 - imgMenu.getIconWidth() / 2, 50, imgMenu.getIconWidth(), imgMenu.getIconHeight());


        //Début lecture des events boutons

        returnToGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    setMenuOpen(!isOpen());
                    getFrame().setMenuOpen(!getFrame().isMenuOpen());
                });
            }
        });


        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    System.exit(0);
                    getFrame().dispose();
                });
            }
        });

        this.setMenuOpen(!this.isOpen());
        this.getFrame().setMenuOpen(!this.ihm.isMenuOpen());
    }
}
