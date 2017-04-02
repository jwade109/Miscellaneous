package snyder;

public class Requester
{
    public static void main(String[] args)
    {
        String seed = "xxxxxyyy";
        int line = 4;
        
        if (args.length > 1)
        {
            seed = args[0];
            line = Integer.parseInt(args[1]);
        }
        
        SnyderTriangle st = new SnyderTriangle();
        String triangle = st.generate(seed, line);
        System.out.println(triangle);
    }
}