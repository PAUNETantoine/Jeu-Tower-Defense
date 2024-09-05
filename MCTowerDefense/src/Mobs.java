import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mobs
{
    private boolean hasDivided; //Sert aux slime et aux mounted spiders.
    private int hp;
    private double speed;
    private int type;
    private int state;
    private int waveToSpawn;
    private int posMobDeplacementX;
    private int posMobDeplacementY;
    private String name;
    private char dir;
    private int nbDeplacement;
    private int tpsSousEffet; //Permet de savoir depuis cmb de tps le mob à un effet sur lui.
    private String originalName; //Permet de save le nom original pour l'état de dégâts.
    private boolean isAnimation;

    public Mobs(int type, int hp, double speed,int state, String name, int waveToSpawn)
    {
        this.waveToSpawn = waveToSpawn;
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.state = state;
        this.posMobDeplacementX = 0;
        this.posMobDeplacementY = 350;
        this.dir = 'E';
        this.nbDeplacement = 0;
        this.tpsSousEffet = 0;
        this.originalName = name;
        this.isAnimation = false;
        this.hasDivided = false;
    }

    public Mobs(Mobs mob)
    {
        this.waveToSpawn = mob.getWaveToSpawn();
        this.name = mob.getName();
        this.type = mob.getType();
        this.hp = mob.getHp();
        this.speed = mob.getSpeed();
        this.state = mob.getState();
        this.posMobDeplacementX = mob.getPosMobDeplacementX();
        this.posMobDeplacementY = mob.getPosMobDeplacementY();
        this.dir = mob.getDir();
        this.nbDeplacement = mob.nbDeplacement;
        this.state = mob.getState();
        this.tpsSousEffet = mob.tpsSousEffet;
        this.originalName = mob.originalName;
        this.hasDivided = mob.hasDivided;
    }

    public Mobs(Mobs mob, int nbDeplacement, char dir, int posMobDeplacementX, int posMobDeplacementY) //Permet l'utilisatipn de la méthode split dans overworld
    {
        this.waveToSpawn = mob.getWaveToSpawn();
        this.name = mob.getName();
        this.type = mob.getType();
        this.hp = mob.getHp();
        this.speed = mob.getSpeed();
        this.state = mob.getState();
        this.posMobDeplacementX = posMobDeplacementX;
        this.posMobDeplacementY = posMobDeplacementY;
        this.dir = dir;
        this.nbDeplacement = nbDeplacement - 1;
        this.state = mob.getState();
        this.tpsSousEffet = mob.tpsSousEffet;
        this.originalName = mob.originalName;
        this.hasDivided = mob.hasDivided;
    }



    public void setAnimation(boolean bool)
    {
        this.isAnimation = bool;
    }

    public boolean isHasDivided()
    {
        return this.hasDivided;
    }

    public void setHasDivided(boolean bool)
    {
        this.hasDivided = bool;
    }

    public void setType(int type)
    {
        this.type = type;
    }


    public boolean isAnimation()
    {
        return isAnimation;
    }

    public boolean dealDmgToPlayer(Overworld world) //On regarde si le mob arrive à faire des dmgs au joueur
    {
        if( this.nbDeplacement == world.getPlayer().getNbDeplacements()) //Si il à le même nombre de déplacement que le joueur
        {
            if( !this.isDead() )
            {
                this.setHp(-this.getHp()); //Le mob meurt
                return true;
            }
        }
        return false;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOriginalName()
    {
        return this.originalName;
    }

    public Runnable animationDmg()
    {
        Timer timerShoot = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setName(getOriginalName());
                ((Timer) e.getSource()).stop();
            }
        });


        if(this.state != 1)
        {
            setName(getOriginalName()+"Dmg");
            timerShoot.start();
        }else{
            setName(getOriginalName()+"Geler");

        }

        return null;
    }

    public int getTpsSousEffet()
    {
        return this.tpsSousEffet;
    }

    public void setTpsSousEffet(int tpsSousEffet)
    {
        this.tpsSousEffet += tpsSousEffet;
    }

    public int getWaveToSpawn() {
        return waveToSpawn;
    }

    public boolean isDead()
    {
        if ( this.getHp() <= 0 )
        {
            return true;
        }
        return false;
    }

    public int getNbDeplacement()
    {
        return nbDeplacement;
    }

    public void setNbDeplacement(int nbDeplacement)
    {
        this.nbDeplacement += nbDeplacement;
    }

    public int getPosMobDeplacementX() {
        return posMobDeplacementX;
    }

    public int getPosMobDeplacementY() {
        return posMobDeplacementY;
    }

    public void setPosMobDeplacementX(int posMobDeplacementX) {
        this.posMobDeplacementX = posMobDeplacementX;
    }

    public void setPosMobDeplacementY(int posMobDeplacementY) {
        this.posMobDeplacementY = posMobDeplacementY;
    }

    public char getDir()
    {
        return dir;
    }

    public void setDir(char dir)
    {
        this.dir = dir;
    }

    public String getName()
    {
        return this.name;
    }
    public int getHp()
    {
        return this.hp;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public int getState()
    {
        return this.state;
    }

    public int getType()
    {
        return this.type;
    }

    public void setHp(int hp)
    {
        this.hp += hp;

        Thread tmp = new Thread(this.animationDmg());
        tmp.start();
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}