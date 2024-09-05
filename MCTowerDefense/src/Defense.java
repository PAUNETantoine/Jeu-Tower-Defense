public class Defense {
    protected double dmg;
    protected int type;
    protected int x;
    protected int y;
    protected int lvl;

    protected Overworld world;

    protected Interface ihm;

    public Defense(int type, int x, int y, int lvl, Overworld world, Interface ihm)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.lvl = lvl;
        this.world = world;
        this.ihm = ihm;

        if(type == 2)
        {
            this.dmg = 0;
        } else if (type == 5)
        {
            this.dmg = 3;
        } else if (type == 6)
        {
            this.dmg = 6;
        }else{
            this.dmg = 9;
        }
    }

    protected void upgrade()
    {
        if( this.lvl == 3 )
        {
            return;
        }

        this.lvl += 1;
        this.dmg += dmg * 0.5;
    }


    protected int getType()
    {
        return this.type;
    }

    protected double getDmg()
    {
        return this.dmg;
    }

    protected int getX()
    {
        return this.x;
    }

    protected int getY()
    {
        return this.y;
    }

    protected int getLvl()
    {
        return this.lvl;
    }

    protected Overworld getWorld()
    {
        return this.world;
    }

    protected Interface getIhm()
    {
        return this.ihm;
    }
}
