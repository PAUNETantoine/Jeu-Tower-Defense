import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfosMenu {

    private Interface ihm;

    private Overworld world;


    public InfosMenu(Interface ihm, Overworld world)
    {
        this.ihm = ihm;
        this.world = world;
    }


    private Overworld getWorld()
    {
        return this.world;
    }

    private Interface getIhm()
    {
        return this.ihm;
    }

    public void drawInfos()
    {
        JLabel textHP = new JLabel(" Health :");
        textHP.setFont(new Font("Minecraft", Font.BOLD, 30));
        textHP.setForeground(Color.BLACK);
        textHP.setBounds(750, 200, 150,50);



        //affichage des coeurs
        for ( int i = 0 ; i < this.getWorld().getPlayer().getHp() ; i++)
        {
            JLabel hearth = new JLabel(new ImageIcon(getClass().getResource("/images/overworld/hearth.png")));
            hearth.setBounds(880 + i*32, 210, 32, 32);
            this.getIhm().getMapPanel().add(hearth, Integer.valueOf(600));
        }


        JLabel textRessources = new JLabel("      " + this.getWorld().getPlayer().getMaterials());
        textRessources.setFont(new Font("Minecraft", Font.BOLD, 30));
        textRessources.setForeground(Color.BLACK);
        textRessources.setBounds(800, 270, 200,50);


        JLabel textMobs = new JLabel("      " + this.getWorld().getNbMobs());
        textMobs.setFont(new Font("Minecraft", Font.BOLD, 30));
        textMobs.setForeground(Color.BLACK);
        textMobs.setBounds(800, 360, 200,50);


        JLabel waves = new JLabel(" Wave : " + this.getWorld().getWave() );
        waves.setFont(new Font("Minecraft", Font.BOLD, 30));
        waves.setForeground(Color.BLACK);
        waves.setBounds(750, 430, 200,60);

        JLabel time = new JLabel(" Next Wave in : " + getWorld().getTime() + " sec");
        time.setFont(new Font("Minecraft", Font.BOLD, 30));
        time.setForeground(Color.BLACK);
        time.setBounds(750, 480, 400,60);

        JLabel kills = new JLabel(" Mob killed : " + getWorld().getPlayer().getKills());
        kills.setFont(new Font("Minecraft", Font.BOLD, 30));
        kills.setForeground(Color.BLACK);
        kills.setBounds(750, 520, 400,60);


        JButton btnWave = new JButton();
        ImageIcon btnImgBg = new ImageIcon(getClass().getResource("/images/overworld/button.png"));
        JLabel textBtn = new JLabel("    Start wave ");
        textBtn.setFont(new Font("Minecraft", Font.PLAIN, 30));
        textBtn.setForeground(Color.WHITE);
        btnWave.setIcon(btnImgBg);
        btnWave.setBounds(850, 570, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn.setBounds(850,575, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());





        this.getIhm().getMapPanel().add(textHP, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(textRessources, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(textMobs, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(waves, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(time, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(btnWave, Integer.valueOf(600));
        this.getIhm().getMapPanel().add(textBtn, Integer.valueOf(700)); //on place le texte du boutton au dessus de celui-c
        this.getIhm().getMapPanel().add(kills, Integer.valueOf(600));


        //Actions bouttons

        btnWave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    getWorld().setTime(-getWorld().getTime());// on met le chrono à 0
                });
            }
        });

    }

}
