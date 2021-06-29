package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

    /**
     * Finds the shortest chain of people from p1 to p2.
     * Chain is returned as a sequence of names starting with p1,
     * and ending with p2. Each pair (n1,n2) of consecutive names in
     * the returned chain is an edge in the graph.
     * 
     * @param g Graph for which shortest chain is to be found.
     * @param p1 Person with whom the chain originates
     * @param p2 Person at whom the chain terminates
     * @return The shortest chain from p1 to p2. Null or empty array list if there is no
     *         path from p1 to p2
     */
    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
        
        ArrayList<String> shortest = new ArrayList<>();

if (g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0)
{
   return null;
}

p1 = p1.toLowerCase();
p2 = p2.toLowerCase();

if (p1.equals(p2))
{
   shortest.add(g.members[g.map.get(p1)].name);
   return shortest;
}

if (g.map.get(p1) == null || g.map.get(p2) == null)
{
   System.out.println("P1 or P2 does not exist!");
   return null;
}

Queue<Integer> queue = new Queue<>();
int[] dist = new int[g.members.length];
int[] pred = new int[g.members.length];
boolean[] visited = new boolean[g.members.length]; 

for (int i = 0; i < visited.length; i++)
{
   visited[i] = false;
   dist[i] = Integer.MAX_VALUE; 
   pred[i] = -1;
}

int startIndex = g.map.get(p1);
Person startPerson = g.members[startIndex];

visited[startIndex] = true;
dist[startIndex] = 0; 

queue.enqueue(startIndex);

while (!queue.isEmpty())
{
   int v = queue.dequeue();
   Person p = g.members[v];

   for (Friend ptr = p.first; ptr != null; ptr = ptr.next)
   {
    int fnum = ptr.fnum;

    if (!visited[fnum])
    {
     dist[fnum] = dist[v] + 1; 
     pred[fnum] = v;
     visited[fnum] = true;
     queue.enqueue(fnum); 
    }
   }
}

Stack<String> path = new Stack<>();
int spot = g.map.get(p2);

if (!visited[spot])
{
   System.out.println("Cannot reach!");
   return null;
}

while (spot != -1)
{
   path.push(g.members[spot].name);
   spot = pred[spot];
}

while (!path.isEmpty())
{
   shortest.add(path.pop());
}

return shortest;
    }
    
    /**
     * Finds all cliques of students in a given school.
     * 
     * Returns an array list of array lists - each constituent array list contains
     * the names of all students in a clique.
     * 
     * @param g Graph for which cliques are to be found.
     * @param school Name of school
     * @return Array list of clique array lists. Null or empty array list if there is no student in the
     *         given school
     */
    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        
ArrayList<ArrayList<String>> answer = new ArrayList<>();

if (g == null || school == null || school.length() == 0)
{
   return null;
}

school = school.toLowerCase();

boolean[] visited = new boolean[g.members.length];
for (int i = 0; i < visited.length; i++)
{
   visited[i] = false;
}

for (Person member : g.members)
{
   if (!visited[g.map.get(member.name)] && member.school != null && member.school.equals(school))
   {

    Queue<Integer> queue = new Queue<>();
    ArrayList<String> clique = new ArrayList<>();

    int startIndex = g.map.get(member.name);
    visited[startIndex] = true;

    queue.enqueue(startIndex);
    clique.add(member.name);

    while (!queue.isEmpty())
    {
     int v = queue.dequeue(); 
     Person p = g.members[v];

     for (Friend ptr = p.first; ptr != null; ptr = ptr.next)
     {
      int fnum = ptr.fnum;
      Person fr = g.members[fnum];

      if (!visited[fnum] && fr.school != null && fr.school.equals(school))
      {
       visited[fnum] = true;
       queue.enqueue(fnum);
       clique.add(g.members[fnum].name);
      }
     }
    }
    answer.add(clique);
   }
}

return answer;
        
    }
    
    /**
     * Finds and returns all connectors in the graph.
     * 
     * @param g Graph for which connectors needs to be found.
     * @return Names of all connectors. Null or empty array list if there are no connectors.
     */
    public static ArrayList<String> connectors(Graph g) {
        
boolean[] visited = new boolean[g.members.length]; 
int[] dfsnum = new int[g.members.length];
int[] back = new int[g.members.length];
ArrayList<String> answer = new ArrayList<>();

for (Person member : g.members)
{
if (!visited[g.map.get(member.name)])
{
dfsnum = new int[g.members.length];
dfs(g.map.get(member.name), g.map.get(member.name), g, visited, dfsnum, back, answer);
}
}


for (int i = 0; i < answer.size(); i++)
{
Friend ptr = g.members[g.map.get(answer.get(i))].first;

int count = 0;
while (ptr != null)
{
ptr = ptr.next;
count++;
}

if (count == 0 || count == 1)
{
answer.remove(i);
}
}

for (Person member : g.members)
{
if ((member.first.next == null && !answer.contains(g.members[member.first.fnum].name)))
{
answer.add(g.members[member.first.fnum].name);
}
}

return answer;
}


private static int sizeArr(int[] arr)
{
int count = 0;
for (int i = 0; i < arr.length; i++)
{
if (arr[i] != 0)
{
count++;
}
}
return count;
}

private static void dfs(int v, int start, Graph g, boolean[] visited, int[] dfsnum, int[] back,
ArrayList<String> answer)
{
Person p = g.members[v];
visited[g.map.get(p.name)] = true;
int count = sizeArr(dfsnum) + 1;

if (dfsnum[v] == 0 && back[v] == 0)
{
dfsnum[v] = count;
back[v] = dfsnum[v];
}

for (Friend ptr = p.first; ptr != null; ptr = ptr.next)
{
if (!visited[ptr.fnum])
{

dfs(ptr.fnum, start, g, visited, dfsnum, back, answer);

if (dfsnum[v] > back[ptr.fnum])
{
back[v] = Math.min(back[v], back[ptr.fnum]);
} else
{

if (Math.abs(dfsnum[v] - back[ptr.fnum]) < 1 && Math.abs(dfsnum[v] - dfsnum[ptr.fnum]) <= 1
&& back[ptr.fnum] == 1 && v == start)
{
continue;
}

if (dfsnum[v] <= back[ptr.fnum] && (v != start || back[ptr.fnum] == 1))
{ 
if (!answer.contains(g.members[v].name))
{

answer.add(g.members[v].name);
}
}

}
} else
{
back[v] = Math.min(back[v], dfsnum[ptr.fnum]);
}
}
return;
}
        
    }