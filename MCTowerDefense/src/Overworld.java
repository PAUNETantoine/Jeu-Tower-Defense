public class Overworld
{

    private int[][] map; //Map storage ( 1 = dirt, 2 = sand/Way, 3 = house, 4 = spawn )

    private Trap[] tabTraps;

    private Mobs[] typesMobs; //Contient tout les mobs utilisables;

    private String name; //World name

    private int wave; //Actual wave

    private int nbMobs; //nbMobs à spawn sur la map

    private int nbDeMobsSpawn;

    private boolean inHouse; //check if player is in the house

    private int time; //Time in sec

    private final int[] spawn = new int[] {6, 0}; // y = 6 x = 0

    private Player player;

    private int size;

    private Interface ihm;

    private Mobs[] tabMobs;

    private Tower[] tabTower;

    private int nbTower;

    private int nbMobsDep;

    private int nbTraps;


    public Overworld(int wave, boolean inHouse, int time)
    {
        this.map = initMap();
        this.name = "OverWorld";
        this.wave = wave;
        this.nbMobs = 0;
        this.inHouse = inHouse;
        this.time = time;
        this.size = 13;
        this.player = new Player(10, this.getSpawn()[1], this.getSpawn()[0], 0);
        this.tabMobs = new Mobs[200];
        this.tabTower = new Tower[200];
        this.tabTraps = new Trap[200];
        this.nbTower = 0;
        this.nbTraps = 0;

        this.typesMobs = new Mobs[] {
                         new Mobs(0, 12, 1, 0, "Zombie", 1),
                         new Mobs(1, 10, 1, 0, "Squelette", 1),
                         new Mobs(2, 11, 1, 0, "Creeper", 2),
                         new Mobs(3, 8, 2, 0, "Spider", 2),
                         new Mobs(4, 15, 2, 0, "MountedSpider", 1),
                         new Mobs(5, 20, 0.5, 0 , "Slime", 1),
                         new Mobs(6, 10, 0.5, 0 , "SlimeSmall", 1) //Slime small
                                                                                                };


        this.nbDeMobsSpawn = 0;
        this.initMap();
    }

    public void addTower(Tower tower)
    {
        this.tabTower[nbTower++] = tower;
    }

    public void addTraps(Trap trap)
    {
        this.tabTraps[nbTraps++] = trap;
    }

    public Trap[] getTabTraps() {
        return tabTraps;
    }

    public Tower[] getTabTower() {
        return tabTower;
    }

    public void setNbDeMobsSpawn(int add)
    {
        this.nbDeMobsSpawn += add;
    }

    public void setWave(int add)
    {
        this.wave += add;
    }

    public int getNbDeMobsSpawn()
    {
        return nbDeMobsSpawn;
    }

    public void setNbMobs(int add)
    {
        this.nbMobs += add;
    }

    public Mobs getMobs(int type)
    {
        return this.typesMobs[type];
    }

    public Mobs[] getTabMobs()
    {
        return this.tabMobs;
    }

    public void initMobs(int wave)
    {
        for(int i = 0 ; i < wave*5 ; i++)
        {
            int type = (int) (Math.random() * 6);
            if(this.getMobs(type).getWaveToSpawn() > wave)
            {
                i--;
            }else
            {
                this.tabMobs[i] = new Mobs(this.getMobs(type));
            }
        }
        nbMobsDep = wave*5;
    }


    private void checkNbMobDead()
    {
        int cpt = 0;
        for( int i = 0 ; i < this.nbDeMobsSpawn ; i++)
        {
            if (this.tabMobs[i].isDead())
            {
                cpt++;
            }
        }
        this.nbMobs = nbMobsDep - cpt;
    }


    private int[][] initMap()
    {
        return this.map = new int[][] {
                                        {0,0,0,0,0,0,0,0,0,0,3,3,3},
                                        {0,0,0,0,0,0,0,0,0,0,3,3,3},
                                        {0,0,0,0,0,0,0,0,0,0,3,3,3},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {4,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                        {0,0,0,0,0,0,0,0,0,0,0,0,0},
                                                                        };
    }


    public String toString()
    {
        String res = "";

        for ( int i = 0 ; i < this.map.length ; i++ )
        {
            for ( int j = 0 ; j < this.map.length ; j++ )
            {
                res += " " + this.map[i][j];
            }
            res += "\n";
        }

        return res;
    }

    public boolean mobOnThesesCoordinates(int x, int y, int dmg)
    {
        for(int i = 0; i < this.nbDeMobsSpawn ; i++)
        {
            if((this.tabMobs[i].getPosMobDeplacementX() < x - 30 && this.tabMobs[i].getPosMobDeplacementX() > x - 70) && (this.tabMobs[i].getPosMobDeplacementY() < y + 20 && this.tabMobs[i].getPosMobDeplacementY() > y - 20) && !this.tabMobs[i].isDead())
            {
                this.tabMobs[i].setHp(-dmg);
                this.checkNbMobDead();

                if(dmg == 0)//Si boule de neige
                {
                    this.tabMobs[i].setState(1); //Effet de ralentissement
                }
                return true;
            }
        }
        return false;
    }


    public boolean mobOnTrap(int x, int y, Mobs mob)
    {
        if( this.getTrapOnThesesCoo(x, y) != null )
        {
            if(mob.getState() != 0)
            {
                return false;
            }


            Trap tmp = this.getTrapOnThesesCoo(x, y);
            mob.setState(tmp.getEffect());
            mob.setHp(-tmp.getDmg());

            if(tmp.getType() == 8)
            {
                tmp.setHp(-1);
            }
            return true;
        }
        return false;
    }


    public Trap getTrapOnThesesCoo(int x, int y)
    {
        for ( int i = 0 ; i < nbTraps ; i++ )
        {
            if( this.tabTraps[i] == null )
            {
                return null;
            }


            if( this.tabTraps[i].getHp() <= 0 )
            {
                this.setMap(this.tabTraps[i].getX(), this.tabTraps[i].getY()-1, 1); //On remet du sable si jamais le cactus est détruit
                this.tabTraps[i] = null;
                return null;
            }

            if( this.tabTraps[i].getX()*50 == x && this.tabTraps[i].getY() * 50 == y && this.tabTraps[i].getHp() > 0)
            {
                return this.tabTraps[i];
            }

        }
        return null;
    }

    public void setTime(int add)
    {
        this.time += add;
    }

    public int[][] getMap()
    {
        return this.map;
    }

    public void setMap(int x, int y, int type)
    {
        this.map[y][x] = type;
    }

    public void setInHouse(boolean inHouse)
    {
        this.inHouse = inHouse;
    }

    public int getType(int x, int y)
    {
        return this.getMap()[y][x];
    }

    public int getTypeOpti(int x, int y)
    {
        if(x >= 12 || y >= 12 || x <= 0 || y <= 0)
        {
            return 16;//Erreur
        }
        return this.getMap()[y][x];
    }


    public int[] getSpawn()
    {
        return this.spawn;
    }

    public int getWave()
    {
        return this.wave;
    }

    public boolean isInHouse()
    {
        return inHouse;
    }

    public int getNbMobs()
    {
        return nbMobs;
    }

    public int getTime()
    {
        return time;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getName()
    {
        return name;
    }

    public void spawnMobsSplit(Mobs mob, int x, int y)
    {
        for( int i = 0; this.tabMobs[i] != null; i++ )
        {
            if( !this.tabMobs[i].isHasDivided() ) //Si le mob est pas encore split
            {

                System.out.println(mob.getNbDeplacement());

                switch (mob.getType())
                {
                    case 5 : this.tabMobs[i] = new Mobs(getMobs(6), mob.getNbDeplacement()-1, mob.getDir(), x, y); this.tabMobs[i+1] = new Mobs(getMobs(6), mob.getNbDeplacement(), mob.getDir(), x, y); break;
                    case 4 : this.tabMobs[i] = new Mobs(getMobs(1), mob.getNbDeplacement()-1, mob.getDir(), x, y); this.tabMobs[i+1] = new Mobs(getMobs(3), mob.getNbDeplacement(), mob.getDir(), x, y); break;
                }

                System.out.println(this.tabMobs[i].getNbDeplacement());

                if(mob.isAnimation())
                {
                    this.tabMobs[i].setNbDeplacement(-2);
                }

                System.out.println(getTypeOpti((mob.getPosMobDeplacementX()/50), (mob.getPosMobDeplacementY()/50) + 1) + "      " + x + "    " + y);

                if(x == 0)
                {
                    this.tabMobs[i].setPosMobDeplacementY(y - 50); this.tabMobs[i+1].setPosMobDeplacementY(y);
                    return;
                } else if (y == 0) {
                    this.tabMobs[i].setPosMobDeplacementX(x - 50); this.tabMobs[i+1].setPosMobDeplacementX(x);
                    return;
                } else if (x == 13*50) {
                    this.tabMobs[i].setPosMobDeplacementY(y - 50); this.tabMobs[i+1].setPosMobDeplacementY(y);
                    return;
                } else if (y == 13*50) {
                    this.tabMobs[i].setPosMobDeplacementX(x - 50); this.tabMobs[i+1].setPosMobDeplacementX(x);
                    return;
                }


                switch (mob.getDir())
                {
                    case 'N', 'S', 'O', 'E' : if(getTypeOpti((mob.getPosMobDeplacementX()/50) - 1, mob.getPosMobDeplacementY()/50) == 1)
                    {
                        System.out.println("X-1");
                        this.tabMobs[i].setPosMobDeplacementX(x - 50); this.tabMobs[i+1].setPosMobDeplacementX(x); break;
                    } else if (getTypeOpti((mob.getPosMobDeplacementX()/50) + 1, mob.getPosMobDeplacementY()/50) == 1)
                    {
                        System.out.println("X+1");
                        this.tabMobs[i].setPosMobDeplacementX(x + 50); this.tabMobs[i+1].setPosMobDeplacementX(x); break;
                    } else if (getTypeOpti((mob.getPosMobDeplacementX()/50), (mob.getPosMobDeplacementY()/50) + 1) == 1)
                    {
                        System.out.println("Y+1");
                        this.tabMobs[i].setPosMobDeplacementY(y); this.tabMobs[i+1].setPosMobDeplacementY(y+50); break;
                    }else{
                        System.out.println("Y-1");
                        this.tabMobs[i].setPosMobDeplacementY(y); this.tabMobs[i+1].setPosMobDeplacementY(y-50); break;
                    }
                    //case 'O', 'E': this.tabMobs[i].setPosMobDeplacementX(x+20); this.tabMobs[i+1].setPosMobDeplacementX(x-20); break;
                }

                this.tabMobs[i].setHasDivided(true);
                return;
            }
        }
    }
}
