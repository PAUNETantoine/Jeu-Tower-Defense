import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AnimationMobs implements Runnable
{

    private Overworld world;

    private Interface ihm;

    private Mobs mob;

    private int x;

    private int y;

    private InfosMenu infosMenu;

    private int lastX;

    private int lastY;

    private static final int DELAY = 150; // Délai en millisecondes entre chaque déplacement

    public AnimationMobs(Overworld world, Interface ihm, Mobs mob, int x, int y, InfosMenu infosMenu)
    {
        this.infosMenu = infosMenu;
        this.ihm = ihm;
        this.world = world;
        this.mob = mob;
        this.x = x;
        this.y = y;
        this.lastX = 50;
        this.lastY = 350;
    }

    public Mobs getMob() {
        return mob;
    }

    public Interface getIhm() {
        return ihm;
    }

    public Overworld getWorld() {
        return world;
    }

    @Override
    public void run()
    {
        try {
            while (!(getMob().getPosMobDeplacementX() == x && getMob().getPosMobDeplacementY() == y))
            {
                //mob.setAnimation(true);

                int currentX = getMob().getPosMobDeplacementX();
                int currentY = getMob().getPosMobDeplacementY();

                if (currentX < x)
                {
                    currentX += (int)(5 * mob.getSpeed());
                }

                if(currentX > x)
                {
                    currentX -= (int)(5 * mob.getSpeed());
                }

                if (currentY < y)
                {
                    currentY += (int)(5 * mob.getSpeed());
                }

                if (currentY > y)
                {
                    currentY -= (int)(5 * mob.getSpeed());
                }



                if( getWorld().mobOnTrap(x, y, mob) )
                {
                    if ( mob.getState() == 2 ) //KnockBack
                    {
                        System.out.println(x + " " + y + " " + lastX + " " + lastY);

                        x = lastX;
                        y = lastY;
                        mob.setState(0);
                        mob.setNbDeplacement(-2);
                        currentX = getMob().getPosMobDeplacementX();
                        currentY = getMob().getPosMobDeplacementY();
                    }
                }



                if( mob.isDead() )
                {
                    getWorld().getPlayer().setMaterials(5);
                    getWorld().getPlayer().setKills(1);

                    if( mob.getType() == 4 || mob.getType() == 5 )
                    {
                        getWorld().spawnMobsSplit(mob, currentX, currentY);
                    }
                    return;
                }

                if( mob.dealDmgToPlayer( getWorld() ) )
                {
                    getWorld().setNbMobs(-1);
                    mob.setHp(-mob.getHp());
                    getWorld().getPlayer().setHp(-1);
                    return;
                }
                

                getMob().setPosMobDeplacementX(currentX);
                getMob().setPosMobDeplacementY(currentY);

                Thread.sleep(DELAY); // Attendre le délai défini avant le prochain déplacement
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (getMob().getDir()) //à refaire
        {
            case 'N' : this.lastX = this.x + 150; this.lastY = this.y - 250; break;
            case 'O' : this.lastX = this.x + 50; this.lastY = this.y; break;
            case 'S' : this.lastX = this.x - 50; this.lastY = this.y + 50; break;
            case 'E' : this.lastX = this.x - 50; this.lastY = this.y; break;
        }
        //this.lastX = x;
        //this.lastY = y;
        mob.setAnimation(false);
    }
}

