public class Trap
{
    private int x;
    private int y;

    private Overworld world;

    private int type;

    private int dmg;

    private int hp;

    private int lvl;

    private int effect; // 1 = ralentit, 2 = knock back, 3 = fire;

    public Trap(Overworld world, int x, int y, int type)
    {
        this.x = x;
        this.y = y;
        this.world = world;
        this.type = type;

        switch (type)
        {
            case 8 : this.dmg = 7; this.effect = 2; break;
            case 9 : this.dmg = 0; this.effect = 1; break;
            case 10 : this.dmg = 15;this.effect = 3; break;
            case 11: this.dmg = 0; this.effect = 0; break;
        }

        this.hp = 3;
        this.lvl = 1;
    }

    public int getHp() {
        return hp;
    }

    public int getType()
    {
        return type;
    }

    public void setHp(int hp)
    {
        this.hp += hp;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getDmg() {
        return dmg;
    }

    public int getEffect() {
        return effect;
    }
}
