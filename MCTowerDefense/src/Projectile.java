public class Projectile implements Runnable
{
    private char dir;
    private int x;
    private int y;
    private int xArr;
    private int yArr;
    private static final int DELAY = 70;
    private String type;
    private Tower tower;
    private boolean restart;
    private int id;


    public Projectile(char dir, int x, int y, String type, Tower tower)
    {
        this.tower = tower;
        this.dir = dir;
        this.x = x + 17;
        this.y = y - 50;
        this.type = type;
        this.restart = false;
        this.id = tower.getNbProjectile();
        this.tower.addProjectile(this);

        switch(dir)
        {
            case 'N' : break;
            case 'O' : this.y += 33; this.x -= 23;break;
            case 'S' : this.y += 50; break;
            case 'E' : this.y += 33; this.x += 23; break;
        }


        switch(dir)
        {
            case 'N' : this.xArr = this.x; this.yArr = 0; break;
            case 'O' : this.xArr = 0; this.yArr = this.y; break;
            case 'S' : this.xArr = this.x; this.yArr = 12*50+140; break;
            case 'E' : this.xArr = 12*50+140; this.yArr = this.y; break;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId()
    {
        return id;
    }

    public String getType() {
        return type;
    }

    public boolean isRestart()
    {
        return restart;
    }

    @Override
    public void run()
    {

        try {
            int xDep = this.x;
            int yDep = this.y;

            while (!this.restart)
            {
                if(this.x >= xDep + 200 || this.y >= yDep + 200 || this.x <= xDep - 200 || this.y <= yDep - 200 || this.x <= 50 || this.y <= 60 || this.x >= 13*50+50 || this.y >= 13*50+70)
                {
                    this.x = 2000;
                    this.y = 2000;
                    this.restart = true;
                    return;
                }

                this.restart = tower.getWorld().mobOnThesesCoordinates(x, y, (int) tower.getDmg());



                if (this.restart)
                {
                    this.x = 2000;
                    this.y = 2000;
                    tower.nbProjectile--;
                    return;
                }



                int currentX = this.x;
                int currentY = this.y;


                if (currentX < this.xArr) {
                    currentX += 5;
                }

                if (currentX > this.xArr) {
                    currentX -= 5;
                }

                if (currentY < this.yArr) {
                    currentY += 5;
                }

                if (currentY > this.yArr) {
                    currentY -= 5;
                }

                this.x = currentX;
                this.y = currentY;

                Thread.sleep(DELAY); // Attendre le délai défini avant le prochain déplacement
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        this.restart = true;
        tower.nbProjectile--;
    }


}
