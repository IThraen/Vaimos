package boat;

import sim.Cell;
import sim.World;
import tools.Vector2D;

public class Boat {

	private int iPosX;
	private int iPosY;
	private double fPosX;
	private double fPosY;
	private World world;
	private Cell[][] grid;									// Cellule d�ja explor�e
	private int tempsM = 0;
	private int tempsH = 0;
	private int tempsJ = 0;
	
	private Sailboat sailboat;
	
	public Boat(int posX, int posY, World world)
	{
		this.iPosX = posX;
		this.fPosX = iPosX;
		this.iPosY = posY;
		this.fPosY = iPosY;
		this.world = world;
		world.setBoat(this);
		grid = new Cell[world.getWorldHeight()][world.getWorldLength()];
		
		sailboat = new Sailboat(iPosX, iPosY);
	}
	
	public void calculate()
	{
		sailboat.update();
		if(world.getCellXY(iPosX, iPosY).getProfondeur()>0)
		{
			move(sailboat.getX(), sailboat.getY());
			analyseCell();
		}
	}
	
	private void move(double posX, double posY)
	{
		time();
		fPosX = posX;
		if(fPosX >= world.getWorldHeight() -1) fPosX = world.getWorldHeight() -1;
		if(fPosX < 0) fPosX = 0;
		iPosX = Math.round((float)fPosX);
		fPosY = posY;
		if(fPosY >= world.getWorldLength() -1) fPosY = world.getWorldLength() -1;
		if(fPosY < 0) fPosY = 0;
		iPosY = Math.round((float)fPosY);
	}
	
	public void setTarget(double ax, double ay, double bx, double by)
	{
		sailboat.setCoordinates(ax, ay, bx, by);
	}
	
	private void time()
	{
		tempsM += 1;
		if(tempsM == 60)
		{
			tempsM = 0;
			tempsH++;
		}
		if(tempsH == 24)
		{
			tempsH = 0;
			tempsJ++;
			world.changementVent();
			sailboat.setWind(world.getCellXY(iPosX, iPosY).getVent());
		}
	}
	
	public int getIPosX()
	{
		return iPosX;
	}
	
	public int getIPosY()
	{
		return iPosY;
	}
	
	public double getFPosX()
	{
		return fPosX;
	}
	
	public double getFPosY()
	{
		return fPosY;
	}
	
	public Cell getGridXY(int i, int j)
	{
		if(grid[i][j] != null) return grid[i][j];
		return null;
		
	}
	
	public double getProfPos()
	{
		Cell c = world.getCellXY(iPosX, iPosY);
		return c.getProfondeur();
	}
	
	public float getSalPos()
	{
		return grid[iPosX][iPosY].getSalinite();
	}
	
	public Vector2D getVentPos()
	{
		return grid[iPosX][iPosY].getVent();
	}
	
	public Vector2D getCourantPos()
	{
		return grid[iPosX][iPosY].getCourant();
	}
	
	private void analyseCell()
	{
		grid[iPosX][iPosY] = world.getCellXY(iPosX, iPosY);
		world.getCellXY(iPosX, iPosY).setDecouvert();
	}
	
	@SuppressWarnings("unused")
	private String toStringCell()
	{
		Cell c = grid[iPosX][iPosY];
		return "\tJour : " + tempsJ + "\tHeure : " + tempsH + "\tMinutes : " + tempsM + "\n\n" +
				"Position : " + fPosX + "\t" + fPosY + "\n" +
				"Vent : " + c.getVent().getX() + "\t" + c.getVent().getY() + "\n" +
				"Courant : " + c.getCourant().getX() + "\t" + c.getCourant().getY() + "\n" +
				"Salinite : " + c.getSalinite() + "\n" +
				"Profondeur : " + c.getProfondeur()+ "\n" +
				c.getVent().toString();	
	}

	public World getWorld() {
		return world;
	}
	
	public void setWind(Vector2D vent)
	{
		sailboat.setWind(vent);
	}
}