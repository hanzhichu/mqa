
package com.ibaguo.nlp.seg.Viterbi.Path;

import java.util.LinkedList;
import java.util.List;

import com.ibaguo.nlp.seg.common.Vertex;


public class Graph
{
    Node nodes[][];

    public Graph(List<Vertex> vertexes[])
    {
        nodes = new Node[vertexes.length][];
        int i = 0;
        for (List<Vertex> vertexList : vertexes)
        {
            if (vertexList == null) continue;
            nodes[i] = new Node[vertexList.size()];
            int j = 0;
            for (Vertex vertex : vertexList)
            {
                nodes[i][j] = new Node(vertex);
                ++j;
            }
            ++i;
        }
    }

    public List<Vertex> viterbi()
    {
        LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
        for (Node node : nodes[1])
        {
            node.updateFrom(nodes[0][0]);
        }
        for (int i = 1; i < nodes.length - 1; ++i)
        {
            Node[] nodeArray = nodes[i];
            if (nodeArray == null) continue;
            for (Node node : nodeArray)
            {
                if (node.from == null) continue;
                for (Node to : nodes[i + node.vertex.realWord.length()])
                {
                    to.updateFrom(node);
                }
            }
        }
        Node from = nodes[nodes.length - 1][0];
        while (from != null)
        {
            vertexList.addFirst(from.vertex);
            from = from.from;
        }
        return vertexList;
    }

}
