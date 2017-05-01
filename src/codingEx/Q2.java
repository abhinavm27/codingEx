package codingEx

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q2 {
	/*
	 * To execute Java, please define "static void main" on a class
	 * named Solution.
	 *
	 * If you need more classes, simply define them inline.
	 */
	static class Component{
	  String name;
	  String endpoint;
	  Map<String,Component> childComponents;
	  
	  public Component(String name,String endpoint) {
	    this.name = name;
	    this.endpoint = endpoint;
	    childComponents = new HashMap<String,Component>();
	  }
	}
	  /*
	   * The buildTree function is a one time operation performed on all the input routes. It returns the root node.
	   * It constructs a tree, where each node holds a map of all its children nodes. It is O(1) operation to check if a child is present or not since it is a map.
	   * The recursive call adds new child when the component is not already present in its children even if it is a wildcard.
	   */
	  static Component buildTree(Component root,String requestPath,String endpoint){
		if(root==null) {
	      Component rootComponent = new Component("/",endpoint);
	      return rootComponent;
	    }    
	    if(requestPath=="")
	      return root;
	    String currComponent = requestPath.split("/")[1];
	    requestPath = requestPath.replaceFirst("/"+currComponent,"");
	    
	    if(!root.childComponents.containsKey(currComponent)) { 
	      Component childComponent = new Component(currComponent,endpoint);
	      root.childComponents.put(currComponent,childComponent);
	    }
	    else {
	      buildTree(root.childComponents.get(currComponent),requestPath,endpoint);
	    }
	    return root;
	  }
	  
	  /*
	   * The getEndpoint function takes the root, traverses its children till it reaches the desired leaf and returns the endpoint if present, else returns 404.
	   * Each recursive call traverses the current node's child node that matches the component. If the component is unmatched and there is a wildcard, then it chooses the wildcard.
	   */
	  static String getEndpoint(Component root, String inputPath) {
	   String endPoint="";    
	   if(inputPath.equals("/"))
	     return root.endpoint;
	   if(inputPath.equals(""))
	     return root.endpoint;
	    String currComponent = inputPath.split("/")[1].trim();
	    inputPath=inputPath.replace("/"+currComponent,"").trim();
	    
	    if(root.childComponents.containsKey(currComponent)) {
	      endPoint = getEndpoint(root.childComponents.get(currComponent),inputPath);
	    }
	    else if(root.childComponents.containsKey("X")) {
	      endPoint = getEndpoint(root.childComponents.get("X"),inputPath);
	    }
	    else {
	      endPoint="404";
	    }
	    
	   return endPoint;
	}
	
	/**
	*The routeAll function first builds a tree structure of components by iterating over the input list of routes.
	*For each of the input paths to be searched, traverse the tree based on the component and return the endpoint if present.
	*Runtime: BuildTree: O(kn) where k is the components in the route path. 
	*getEndpoint: O(log n) as it traverses only those nodes that belong to its components.
	*/
    private static List<String> routeAll(List<Route> routes, List<String> paths) {
        List<String> endpoints = new ArrayList<String>();
        Component root = null;
        for(Route route:routes) {
        	root = buildTree(root, route.path, route.endpoint);
        }
        
        for(String path:paths) {
        	String endpoint = getEndpoint(root, path);
        	endpoints.add(endpoint);
        }
        return endpoints;
    }

    /**
     *      Hey! You probably won't need to edit anything below here.
     */

    static class Route {
        String path;
        String endpoint;
        public Route(String path, String endpoint) {
            this.path = path;
            this.endpoint = endpoint;
        }
    }

    private static List<Route> getRoutes(InputStream is) throws IOException {
        List<Route> routes = new ArrayList<Route>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            String[] tokenizedLine = line.split(" ");
            routes.add(new Q2.Route(tokenizedLine[0], tokenizedLine[1]));
        }
        return routes;
    }

    private static List<String> getPaths(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> paths = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            paths.add(line);
        }
        return paths;
    }

    public static void main(String... args) throws IOException {
        List<Route> routes = Q2.getRoutes(new FileInputStream(args[0]));
        List<String> paths = Q2.getPaths(System.in);

        for(String endpoint : Q2.routeAll(routes, paths)) {
            System.out.println(endpoint);
        }
    }
}
