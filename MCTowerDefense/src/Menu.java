import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    public Menu()
    {
        // Configurer la fenêtre principale
        this.setTitle("Minecraft Tower Defense - MENU -");
        this.setSize(1550, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //Icon
        this.setIconImage(new ImageIcon(getClass().getResource("/images/menu/logo.png")).getImage());


        // Créer un panneau avec un gestionnaire de disposition null pour pouvoir positionner les composants manuellement
        JLayeredPane panel = new JLayeredPane();
        panel.setBounds(0, 0 ,this.getWidth(), this.getHeight());


        // Ajouter une image de fond au panneau
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/images/menu/background.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        panel.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);


        // Ajouter le logo
        ImageIcon logoImage = new ImageIcon(getClass().getResource("/images/menu/logoMC.png"));
        JLabel logoLabel = new JLabel(logoImage);
        logoLabel.setBounds(this.getWidth() / 2 - logoImage.getIconWidth() / 2, 25, logoImage.getIconWidth(), logoImage.getIconHeight());
        panel.add(logoLabel, JLayeredPane.POPUP_LAYER);


        //Ajout des boutons
        ImageIcon btnImgBg = new ImageIcon(getClass().getResource("/images/overworld/button.png"));


        JButton buttonPlay = new JButton();
        JLabel textBtn = new JLabel("         Play ");
        textBtn.setFont(new Font("Minecraft", Font.PLAIN, 30));
        textBtn.setForeground(Color.WHITE);
        buttonPlay.setIcon(btnImgBg);
        buttonPlay.setBounds(this.getWidth() / 2 - btnImgBg.getIconWidth() / 2, 300, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn.setBounds(this.getWidth() / 2 - btnImgBg.getIconWidth() / 2,305, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        panel.add(buttonPlay, JLayeredPane.POPUP_LAYER);
        panel.add(textBtn, JLayeredPane.DRAG_LAYER);

        JButton buttonOption = new JButton();
        JLabel textBtn2 = new JLabel("      Options ");
        textBtn2.setFont(new Font("Minecraft", Font.PLAIN, 30));
        textBtn2.setForeground(Color.WHITE);
        buttonOption.setIcon(btnImgBg);
        buttonOption.setBounds(this.getWidth() / 2 - btnImgBg.getIconWidth() / 2, 400, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        textBtn2.setBounds(this.getWidth() / 2 - btnImgBg.getIconWidth() / 2,405, btnImgBg.getIconWidth() - 12, btnImgBg.getIconHeight());
        panel.add(buttonOption, JLayeredPane.POPUP_LAYER);
        panel.add(textBtn2, JLayeredPane.DRAG_LAYER);


        // Zone des credits
        JLabel textField = new JLabel("Made by gamingtoine");
        textField.setForeground(Color.RED);                                            //Couleur texte
        textField.setFont(textField.getFont().deriveFont(20f));                   //Taille texte
        textField.setBounds( 0, this.getHeight() - 70, 250, 30);    //Pos Texte
        panel.add(textField, JLayeredPane.MODAL_LAYER);




        // Lancement du programme lors du clic sur le boutton play
        buttonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Controleur ctrl = new Controleur(new Overworld(1, false, 60), new Interface("Minecraft Tower Defense | Overworld |"));
                    dispose();
                });
            }
        });

        // Afficher la page
        this.add(panel);
        this.setVisible(true);

    }
}
