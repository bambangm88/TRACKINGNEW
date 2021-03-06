package com.example.bama.tracking.Algo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.bama.tracking.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String tempPath ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testExcute();
    }



    private List<Vertex> nodes;
    private List<Edge> edges;

    public void testExcute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        for (int i = 0; i < 11; i++) {
            Vertex location = new Vertex("Node_" + i, "Node_" + i);
            nodes.add(location);
        }





        addLane("Edge_0", 0, 1, 85);
        addLane("Edge_1", 0, 2, 217);
        addLane("Edge_2", 0, 4, 173);
        addLane("Edge_3", 2, 6, 186);
        addLane("Edge_4", 2, 7, 103);
        addLane("Edge_5", 3, 7, 183);
        addLane("Edge_6", 5, 8, 250);
        addLane("Edge_7", 8, 9, 84);
        addLane("Edge_8", 7, 9, 167);
        addLane("Edge_9", 4, 9, 10);
        addLane("Edge_10", 9, 10, 600);
        addLane("Edge_11", 1, 10, 10);

        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));


        String temp = "";

        for (Vertex vertex : path)
        {
            temp += vertex + " ";
              System.out.print(temp);

            Toast.makeText(MainActivity.this,temp,Toast.LENGTH_SHORT).show();

        }

        if (temp !=null && temp.length()>0 && temp.charAt(temp.length()-1) == 'x')
        {
            temp = temp.substring(0,temp.length()-1);
        }


        temp = removeLastChar(temp);
        tempPath = temp;

        System.out.print(tempPath);

    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo,
                         int duration) {
        Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
        edges.add(lane);
    }



    public static String getPath()
    {

        return tempPath;
    }

    private static String removeLastChar(String str)
    {
        return str.substring(0, str.length()-1);
    }





}
