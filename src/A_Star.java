import java.util.ArrayList;
import java.math.*;

public class A_Star {
	private int heuristic;
	private Agent agent;
	private Environment environment;
	
	private ArrayList<Integer> StateSeq=new ArrayList<Integer>();
	private ArrayList<Integer[]> path=new ArrayList<Integer[]>();

	private ArrayList<State> exploredState = new ArrayList<State>();
	private ArrayList<State> expandState = new ArrayList<State>();
	
	private int expandCount;
	private int exploredCount;
	
	public A_Star(Environment environment, Agent agent, int heuristic)
	{
		this.agent = agent;
		this.environment = environment;
		this.heuristic = heuristic;
		
		expandCount = 0;
		exploredCount = 0;
		
		exploredState.add(new State(agent.getLocation(), agent.getDirection()));
		exploredState.get(exploredCount).setBefore(exploredState.get(exploredCount));
		exploredState.get(exploredCount).setG(0);
		exploredState.get(exploredCount).calculateF();
	}
	
	public void findOpPath()
	{
		int lowestF, lowestI;
		while(!environment.isEndLocation(exploredState.get(exploredCount).getLocation()))
		{
			expandNode();
			
			lowestF = expandState.get(0).getF();
			lowestI = 0;

			//System.out.println(lowestF + " " + lowestI);
			
			for(int i = 1 ; i < expandCount ; i++)
			{
				if(expandState.get(i).getF() <= lowestF)
				{
					lowestF = expandState.get(i).getF();
					lowestI = i;
				}
				//System.out.println(lowestF + " " + lowestI);
			}
			
			
			exploredState.get(exploredCount).setAction(expandState.get(lowestI).getAction());
			exploredState.add(expandState.get(lowestI));

			exploredCount++;
			
			exploredState.get(exploredCount).setBefore(expandState.get(lowestI).getBefore());
			exploredState.get(exploredCount).setG(expandState.get(lowestI).getG());
			exploredState.get(exploredCount).setH(expandState.get(lowestI).getH());
			exploredState.get(exploredCount).calculateF();
			
			agent.setDirection(exploredState.get(exploredCount).getDirection());
			agent.setLocation(exploredState.get(exploredCount).getLocation());
			
			expandState.remove(lowestI);
			expandCount--;
			
			environment.placeAgent(agent);
			//System.out.println(exploredCount);
			System.out.println(agent.getLocation()[1] + " " + agent.getLocation()[0]
					+ " " + exploredState.get(exploredCount).getF());
			//environment.printEnvironment();
		}
		
		ArrayList<State> tempPath = new ArrayList<State>();
		State tempState = exploredState.get(exploredCount);
		int pathCount = 0;
		Integer[] tempLocation;
		
		while(!exploredState.get(0).isSameLocation(tempState.getLocation()))
		{
			tempPath.add(tempState);
			tempState = tempPath.get(pathCount).getBefore();
			pathCount++;
		}
		tempPath.add(tempState);
		tempState = tempPath.get(pathCount).getBefore();
		pathCount++;
		for(int i = pathCount - 1  ; i >= 0 ; i--)
		{
			tempLocation = new Integer[2];
			tempLocation[0] = tempPath.get(i).getLocation()[0];
			tempLocation[1] = tempPath.get(i).getLocation()[1]; 
			path.add(tempLocation);
			if(i == 0)
			{
				StateSeq.add(Action.GRAB);
				StateSeq.add(Action.END_TRIAL);
			}
			else
			{
				for(int j = 0 ; j < tempPath.get(i).getAction().size() ; j++)
					StateSeq.add(tempPath.get(i).getAction().get(j));
			}
		}
		agent.setLocation(exploredState.get(0).getLocation());
		agent.setDirection(exploredState.get(0).getDirection());
	}

	//Location[0] -> y Location[1] -> x
	public void expandNode() {
		int[] newLocation = new int[2];
		
		// 우측 끝에 붙어있음
		if (agent.getLocation()[1] + 1 == environment.getWorldSize()) {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];
				
				expandState.add(new State(newLocation, 'W'));
				expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
				g();
				h();
				expandState.get(expandCount).calculateF();
					
				if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
						|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
				{
					expandState.remove(expandCount);
				}
				else
					expandCount++;
				
				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;
				
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			} else {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			}
		}
		// 좌측 끝에 붙어있음
		else if (agent.getLocation()[1] == 0) {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			} else {
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;
				
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			}
		} else {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			} else {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
				
				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
					
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					
					if ( (isExplored(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF())
							|| (isExpanded(expandState.get(expandCount).getLocation(), expandState.get(expandCount).getF()))) ) 
					{
						expandState.remove(expandCount);
					}
					else
						expandCount++;
					
			}
		}
		/*
				if (agent.getLocation()[1] + 1 == environment.getWorldSize()) {
					// 상단 끝에 붙어있음
					if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];
						
						
						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;
						
						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					}
					// 하단 끝에 붙어있음
					else if (agent.getLocation()[0] == 0) {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;

						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					} else {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;

						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;

						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					}
				}
				// 좌측 끝에 붙어있음
				else if (agent.getLocation()[1] == 0) {
					// 상단 끝에 붙어있음
					if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;

						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					}
					// 하단 끝에 붙어있음
					else if (agent.getLocation()[0] == 0) {
						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;

						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'E')) {
							expandState.add(new State(newLocation, 'E'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					} else {
						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;
						
						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;

						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'E')) {
							expandState.add(new State(newLocation, 'E'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					}
				} else {
					// 상단 끝에 붙어있음
					if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'E')) {
							expandState.add(new State(newLocation, 'E'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;
						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

					}
					// 하단 끝에 붙어있음
					else if (agent.getLocation()[0] == 0) {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'E')) {
							expandState.add(new State(newLocation, 'E'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;

						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					} else {
						// 왼쪽 노드
						newLocation[1] = agent.getLocation()[1] - 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'W')) {
							expandState.add(new State(newLocation, 'W'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
						
						// 오른쪽 노드
						newLocation[1] = agent.getLocation()[1] + 1;
						newLocation[0] = agent.getLocation()[0];

						if (!isExplored(newLocation, 'E')) {
							expandState.add(new State(newLocation, 'E'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
						// 윗 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] + 1;

						if (!isExplored(newLocation, 'N')) {
							expandState.add(new State(newLocation, 'N'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}

						// 아래 노드
						newLocation[1] = agent.getLocation()[1];
						newLocation[0] = agent.getLocation()[0] - 1;

						if (!isExplored(newLocation, 'S')) {
							expandState.add(new State(newLocation, 'S'));
							expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
							g();
							h();
							expandState.get(expandCount).calculateF();
							expandCount++;
						}
					}
				}
		*/
		
		
		/*
		// 우측 끝에 붙어있음
		if (agent.getLocation()[1] + 1 == environment.getWorldSize()) {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];
				
				
				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;
				
				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			} else {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			}
		}
		// 좌측 끝에 붙어있음
		else if (agent.getLocation()[1] == 0) {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			} else {
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;
				
				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			}
		} else {
			// 상단 끝에 붙어있음
			if (agent.getLocation()[0] + 1 == environment.getWorldSize()) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;
				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

			}
			// 하단 끝에 붙어있음
			else if (agent.getLocation()[0] == 0) {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			} else {
				// 왼쪽 노드
				newLocation[1] = agent.getLocation()[1] - 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'W'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
				
				// 오른쪽 노드
				newLocation[1] = agent.getLocation()[1] + 1;
				newLocation[0] = agent.getLocation()[0];

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'E'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
				// 윗 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] + 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'N'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}

				// 아래 노드
				newLocation[1] = agent.getLocation()[1];
				newLocation[0] = agent.getLocation()[0] - 1;

				if (!exploredState.get(exploredCount).isSameLocation(newLocation)) {
					expandState.add(new State(newLocation, 'S'));
					expandState.get(expandCount).setBefore(exploredState.get(exploredCount));
					g();
					h();
					expandState.get(expandCount).calculateF();
					expandCount++;
				}
			}
		}
		*/
		
		
	}

	public ArrayList<Integer[]> getPath()
	{
		return path;
	}
	
	public ArrayList<Integer> getStateSeq()
	{
		return StateSeq;
	}
	
	public void g()
	{
		int actionCost = 0;
		char direction = agent.getDirection();
		ArrayList<Integer> actionSeq = new ArrayList<Integer>();

		while(direction != expandState.get(expandCount).getDirection())
		{
				// turn
				actionCost += 1;

				if(direction == 'S')
				{
					if(expandState.get(expandCount).getDirection() == 'W')
					{
						actionSeq.add(Action.TURN_RIGHT);
						direction = 'W';
					}
					else 
					{

						actionSeq.add(Action.TURN_LEFT);
						direction = 'E';
					}
				}
				else if(direction == 'W')
				{
					if(expandState.get(expandCount).getDirection() == 'N')
					{
						actionSeq.add(Action.TURN_RIGHT);
						direction = 'N';
					}
					else
					{		
						actionSeq.add(Action.TURN_LEFT);
						direction = 'S';
					}
				}
				else if(direction == 'N')
				{
					if(expandState.get(expandCount).getDirection() == 'E')
					{
						actionSeq.add(Action.TURN_RIGHT);
						direction = 'E';
					}
					else
					{
						actionSeq.add(Action.TURN_LEFT);
						direction = 'W';
					}
				}
				else
				{
					if(expandState.get(expandCount).getDirection() == 'S')
					{
						actionSeq.add(Action.TURN_RIGHT);
						direction = 'S';
					}
					else
					{
						actionSeq.add(Action.TURN_LEFT);
						direction = 'N';
					}
				}				
		}
		
		if(environment.isWumpus(expandState.get(expandCount).getLocation()))
		{
			// shoot arrow == 2
			actionCost += 2;
			actionSeq.add(Action.SHOOT);
		}
		
		if(environment.isPit(expandState.get(expandCount).getLocation()))
		{
			// never go that state
			actionCost += 1000000;
		}
		
		// go forward action
		actionSeq.add(Action.GO_FORWARD);
		actionCost += 1;

		expandState.get(expandCount).setG(expandState.get(expandCount).getBefore().getG() + actionCost);
		expandState.get(expandCount).setAction(actionSeq);
		
	}
	
	public void h()
	{
		int result = 0;
		switch(heuristic)
		{
		case 1: // 유클리드
			result = (int)(Math.pow((Math.pow(Math.abs(environment.getEndLocation()[0] - expandState.get(expandCount).getLocation()[0]), 2.0) +
					+  Math.pow(Math.abs(environment.getEndLocation()[1] - expandState.get(expandCount).getLocation()[1]), 2.0)),0.5)); 
			break;
		case 2: // 맨하탄
			result = Math.abs(environment.getEndLocation()[0] - expandState.get(expandCount).getLocation()[0])
			+ Math.abs(environment.getEndLocation()[1] - expandState.get(expandCount).getLocation()[1]);
			break;
		}
		
		expandState.get(expandCount).setH(result);
	}

	/*
	public boolean isExplored(int[] newLocation, char newDirection)
	{
		for(int i = 0 ; i < exploredCount ; ++i)
		{
			if(exploredState.get(i).isSameLocation(newLocation) && 
					exploredState.get(i).getDirection() == newDirection)
			{
				return true;
			}
		}
		return false;
	}
	*/
	
	public boolean isExplored(int[] newLocation, int newF)
	{
		for(int i = 0 ; i < exploredCount ; ++i)
		{
			if(exploredState.get(i).isSameLocation(newLocation) && 
					exploredState.get(i).getF() == newF)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isExpanded(int[] newLocation, int newF)
	{
		for(int i = 0 ; i < expandCount ; ++i)
		{
			if(expandState.get(i).isSameLocation(newLocation) && 
					expandState.get(i).getF() == newF)
			{
				return true;
			}
		}
		return false;
	}
}
