import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class Solver
{
    /**
     * The main class
     */
    public static void main(String[] args)
    {
        String fileName = null;
        
        // get the temp file name
        for(String arg : args){
            if(arg.startsWith("-file=")){
                fileName = arg.substring(6);
            }
        }
        if(fileName == null)
            return;
        try {
            solve(fileName);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     */
    public static void solve(String fname)
	throws IOException
    {
        // read the lines out of the file
        List<String> lines = new ArrayList<String>();

        BufferedReader input =  new BufferedReader(new FileReader(fname));
        try {
            String line = null;
            while((line = input.readLine()) != null) {
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        
        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        Item items[] = new Item[Integer.parseInt(firstLine[0])];
        int capacity = Integer.parseInt(firstLine[1]);
        for(int i = 1; i < items.length + 1; i++) {
          String[] parts = lines.get(i).split("\\s+");
	  items[i - 1] = new Item(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
	Grid g = new Grid(items, capacity);
	g.solve();
        // prepare the solution in the specified output format
        // System.out.println("" + value + " " + (capacity - weight));
        // for(int i=0; i < items; i++){
        //     System.out.print(taken[i]+" ");
        // }
        // System.out.println("");        
    }
}

class Grid
{
    public Grid(Item items[], int capacity)
    {
	this.items = new Item[items.length];
	for(int i = 0; i < items.length; i++)
	    this.items[i] = new Item(items[i]);
	grid = new int[items.length][capacity];
	for(int i = 0; i < items.length; i++)
	    for(int j = 0; j < capacity; j++)
		grid[i][j] = 0;
    }
	
    public void solve()
    {
	for(int i = 0; i < grid.length; i++) {
	    for(int j = 0; j < grid[0].length; j++) {
		grid[i][j] = chooseValue(i, j);
	    }
	}
	System.out.print("\nWEIGHT:            ");
	for(int i = 1; i <= grid[0].length; i++) {
	    System.out.printf("%3d ", i);
	}
	System.out.println("\n------------------");
	for(int i = 0; i < grid.length; i++) {
	    System.out.printf("Item " + (i + 1) + " (%3d, %3d): ", items[i].value, items[i].weight);
	    for(int j = 0; j < grid[0].length; j++) {
		System.out.printf("%3d ", grid[i][j]);
	    }
	    System.out.println("");
	}

    }

    public int chooseValue(int i, int c)
    {
	int checkval = get(i, c - items[i].weight + 1) + items[i].value;
	if(c >= items[i].weight - 1 && checkval > get(i, c + 1)) {
	    return checkval;
	}
	return get(i, c + 1);
    }

    public int get(int item, int cap)
	throws ArrayIndexOutOfBoundsException
    {
	if(item <= 0 || cap <= 0) {
	    return 0;
	}
	if(item > grid.length || cap > grid[0].length) {
	    System.err.println("Attempting to access invalid location! (" + item + ", " + cap);
	    System.err.flush();
	    throw new ArrayIndexOutOfBoundsException();
	}
	return grid[item - 1][cap - 1];
    }

    int grid[][];
    Item items[];
}

class Item
{
    public Item()
    {}

    public Item(int v, int w)
    {
	value = v;
	weight = w;
    }
    
    public Item(Item i)
    {
	value = i.value;
	weight = i.weight;
    }
    
    public int value;
    public int weight;
}
