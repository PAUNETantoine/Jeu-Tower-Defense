public class Tower extends Defense
{
    private int xPrjct;
    private int yPrjct;
    private String nom;
    private Overworld world;
    private Interface ihm;
    private char[] dir;
    private String projectileNom;
    public int nbProjectile = -1;
    private Projectile[] tabProjectile;

    public Tower(int type, int x, int y, String nom, int lvl, char[] dir, Overworld world, Interface ihm)
    {

        super(type, x, y, lvl, world, ihm);
        this.nom = nom;
        this.dir = dir;
        this.xPrjct = x;
        this.yPrjct = y;
        this.tabProjectile = new Projectile[60];


        if(type == 2)
        {
            this.projectileNom = "snowBall";
        } else if (type == 5)
        {
            this.projectileNom = "egg";
        } else if (type == 6)
        {
            this.projectileNom = "arrow"+dir[0];
        }else{
            this.projectileNom = "fireBall";
        }
    }

    public void addProjectile(Projectile prjct)
    {
        for(int i = 0 ; i < this.tabProjectile.length ; i++)
        {
            if(this.tabProjectile[i] == null || this.tabProjectile[i].isRestart())
            {
                this.tabProjectile[i] = prjct;
                this.nbProjectile++;
                this.clearProjectile();
                return;
            }
        }
    }

    public void clearProjectile()
    {
        for(int i = 0; i < this.tabProjectile.length ; i++)
        {
            if(this.tabProjectile[i] == null || this.tabProjectile[i].isRestart())
            {
                this.tabProjectile[i] = null;
                this.nbProjectile--;
            }
        }
    }


    public void dropTabProj()
    {
        this.tabProjectile = new Projectile[60];
    }


    public Projectile[] getTabProjectile()
    {
        return this.tabProjectile;
    }

    public String toStrTabProj()
    {
        String res = "[ ";

        for ( int i = 0 ; i < tabProjectile.length ; i++)
        {
            res += tabProjectile[i].isRestart() + ", ";
        }
        return res + " ]";
    }

    public Projectile getProjectile(int id)
    {
        return this.tabProjectile[id];
    }

    public int getNbProjectile()
    {
        return nbProjectile;
    }

    public int getXTower()
    {
        return super.x;
    }

    public int getYTower()
    {
        return super.y;
    }

    public String getProjectileNom()
    {
        return this.projectileNom;
    }

    public Overworld getWorld()
    {
        return super.getWorld();
    }
    public Interface getIhm()
    {
        return super.getIhm();
    }
    public char[] getDir()
    {
        return dir;
    }
}



