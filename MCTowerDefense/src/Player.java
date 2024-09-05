import javax.swing.*;

public class Player extends JLayeredPane {
    private int hp;
    private int posX;
    private int posY;
    private int kills;
    private int materials;
    private char dir;
    private int posXDeplacement;
    private int posYDeplacement;

    private int nbDeplacements;

    private boolean isAnimation;


    public Player(int hp, int posX, int posY, int materials)
    {
        this.hp = hp;
        this.posX = posX;
        this.posY = posY;
        this.kills = 0;
        this.materials = materials;
        this.dir = 'E';
        this.nbDeplacements = 0;
        this.isAnimation = false;
    }

    public int getNbDeplacements() {
        return nbDeplacements;
    }

    public int getPosXDeplacement()
    {
        return posXDeplacement;
    }

    public int getPosYDeplacement()
    {
        return posYDeplacement;
    }

    public void setPosXDeplacement(int x)
    {
        this.posXDeplacement = x;
    }

    public void setPosYDeplacement(int y)
    {
        this.posYDeplacement = y;
    }

    public boolean isAnimation()
    {
        return isAnimation;
    }

    public void setInAnimation(boolean b)
    {
        this.isAnimation = b;
    }


    public void setNbDeplacements(int nbDeplacements) {
        this.nbDeplacements += nbDeplacements;
    }

    public boolean move(char dir)
    {
       switch ( dir )
       {
           case 'N' :
           {
               if( this.getPosY() == 1 ) return false;
               else
               {
                   this.setPosY(-1);
                   return true;
               }
           }

           case 'O' :
           {
               if( this.getPosX() == 1 ) return false;
               else
               {
                   this.setPosX(-1);
                   return true;
               }
           }

           case 'S' :
           {
               if( this.getPosY() == 13 ) return false;
               else
               {
                   this.setPosY(1);
                   return true;
               }
           }

           case 'E' :
           {
               if( this.getPosX() == 13 ) return false;
               else
               {
                   this.setPosX(1);
                   return true;
               }
           }
           default: return false;
       }
    }

    public int getPosX()
    {
        return posX;
    }

    public int getHp()
    {
        return hp;
    }

    public boolean isAtPos(int x, int y)//Si est sur x et y alors true
    {
        return this.getPosX() == x && this.getPosY() == y;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setHp(int add)
    {
        this.hp = hp + add;
    }

    public int getKills()
    {
        return kills;
    }

    public int getMaterials()
    {
        return materials;
    }

    public void setKills(int kills)
    {
        this.kills += kills;
    }

    public char getDir()
    {
        return this.dir;
    }

    public void setDir(char dir)
    {
        this.dir = dir;
    }

    public void setPosX(int add)
    {
        this.posX = add;
    }

    public void setPosY(int add)
    {
        this.posY = add;
    }

    public void setMaterials(int add)
    {
        this.materials = materials + add;
    }
}
