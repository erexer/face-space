package facespace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.Timer;

public class FaceSpace<Key extends Comparable<Key>, Value> {

	//this handles searching and sorting the users and descriptions
	private static RedBlackBST t;
	//this keeps track of the number of users using FaceSpace
	private static int numUsers;
	//this keeps track of all the UserInfos associated with every user
	private static ArrayList<UserInfo> masterUserList;
	//this keeps track of all of all users' friends, using the same indexing as masterUserList
	private static ArrayList<ArrayList<String>> masterFriendList;
	//this keeps track of all of all users' partners, using the same indexing as masterUserList
	private static ArrayList<ArrayList<String>> masterPartnerList;

	private static Graph friendGraph;
	private static Graph partnerGraph;
	
	private FaceSpace(File file) {

	}
	
	//updates the description of a user
	public static void updateUser(String username, String updatedDescription) {
		//get the index of the username
		int usernameIndex = findIndex(username);
		//get the UserInfo associated with the username
		UserInfo info = masterUserList.get(usernameIndex);
		//this allows the description associated with the username to be manipulated more easily
		String description = masterUserList.get(findIndex(username)).description;
		//formatting
		description = "========================================" + "\n" + username + "\n";
		for (int i = 0; i < username.length(); i++) {
			description += ("-");
		}
		//this updates only the description - it leaves isPolyamorous and the friend
		//and partner information unchanged
		description += "\n" + updatedDescription + updateDescription(info);
		//update the description of the username in the BST
		t.put(username, description);
		System.out.println(username + "'s description was successfully updated.");
	}

	//adds a friend to any user's friend list and adds the user to the friend's friend list
	public static void addFriend(String user, String friendToBeAdded) {
		UserInfo updatedUserInfo;
		UserInfo updatedFriendInfo;
		//get the index of the user
		int userIndex = findIndex(user);
		//get the index of the friend to be added
		int friendToBeAddedIndex = findIndex(friendToBeAdded);
		//if the user is not found in masterUserList
		if (userIndex == -1){
			System.out.println("This user doesn't exist!");
			return;
		}
		//if the friend is not found in masterUserList
		else if (friendToBeAddedIndex == -1){
			System.out.println("This friend doesn't exist!");
			return;
		}
		//if the user and friend are already friends
		else if(masterFriendList.get(userIndex).contains(friendToBeAdded)){
			System.out.println("These users are already friends!");
		}
		//add the friend to the user's friend list
		//add the user to the friend's friend list
		else {
			masterFriendList.get(userIndex).add(friendToBeAdded);
			masterFriendList.get(friendToBeAddedIndex).add(user);
			System.out.println("Friend added!");
			friendGraph = updateFriendGraph();
			partnerGraph = updatePartnerGraph();
		}
		//allows the user's UserInfo and the friend's UserInfo to be accessed more easily
		updatedUserInfo = masterUserList.get(userIndex);
		updatedFriendInfo = masterUserList.get(friendToBeAddedIndex);
		// update the BST with new friend information
		t.put(user, updateDescription(updatedUserInfo));
		t.put(friendToBeAdded, updateDescription(updatedFriendInfo));

	}

	//adds a partner to any user's partner list and adds the user to the partner's partner list
	public static void addPartner(String user, String partnerToBeAdded) {
		//get the index of the user
		int userIndex = findIndex(user);
		//get the index of the parter to be added
		int partnerToBeAddedIndex = findIndex(partnerToBeAdded);
		//if the user is not found in masterUserList
		if (userIndex == -1){
			System.out.println("This user doesnt exist!");
			return;
		}
		//if the partner is not found in masterUserList
		else if (partnerToBeAddedIndex == -1){
			System.out.println("This partner doesnt exist!");
			return;
		}
		//if the user and partner are already partners
		else if(masterPartnerList.get(userIndex).contains(partnerToBeAdded)){
			System.out.println("These users are already partners!");
		}
		else if(masterUserList.get(userIndex).isPolyamorous == false){
			if(!masterPartnerList.get(userIndex).equals(null)){
				System.out.println("This user is not polyamorous, therefore no more partners may be added.");
			}
		}
		//add the partner to the user's partner list
		//add the user to the partner's partner list
		else {
			masterPartnerList.get(userIndex).add(partnerToBeAdded);
			masterPartnerList.get(partnerToBeAddedIndex).add(user);
			if(!masterFriendList.get(userIndex).contains(partnerToBeAdded)){
				System.out.println("Partner added!");
				addFriend(user, partnerToBeAdded);
			}
			else{
				System.out.println("Partner added!");
			}
		}
		//allows the user's UserInfo and the partner's UserInfo to be accessed more easily
		UserInfo updatedUserInfo = masterUserList.get(userIndex);
		UserInfo updatedPartnerInfo = masterUserList.get(partnerToBeAddedIndex);
		friendGraph = updateFriendGraph();
		partnerGraph = updatePartnerGraph();
		//update the BST with new parter information
		t.put(user, updateDescription(masterUserList.get(userIndex)));
		t.put(partnerToBeAdded, updateDescription(masterUserList.get(partnerToBeAddedIndex)));
	}

	//helper method to more easily get the index associated with a given user
	public static int findIndex(String username) {
		//navigate through the array list, looking to see when the username given equals
		//the username of some UserInfo
		for (int i = 0; i <= masterUserList.size() -1 ; i++) {
			if (masterUserList.get(i).username.equals(username)) {
				return i;
			}
			else continue;
		}
		return -1;
	}

	public static void removeFriend(String user, String friendToBeRemoved) {
		// get the index of the user
		int userIndex = findIndex(user);
		//get the index of the friend to be removed
		int friendToBeRemovedIndex = findIndex(friendToBeRemoved);

		// if the user or friend to be removed doesn't exist, exit
		if (userIndex == -1 || friendToBeRemovedIndex == -1) {
			System.out.println("One of those people doesn't exist. :o( ");
			return;
		}

		// if the user is in friend's friend list, remove
		if (masterUserList.get(userIndex).friendArrayList.contains(friendToBeRemoved)) {
			// remove the friend from the user's friend list
			masterUserList.get(userIndex).friendArrayList.remove(friendToBeRemoved);
			System.out.println(friendToBeRemoved + " has been removed from " + user + "'s friend list. :o)");
		} 
		else {
			System.out.println(friendToBeRemoved + " is not in " + user + "'s friend list. :o(");
		}

		// if the user is in the friend's friend list, remove
		if (masterUserList.get(friendToBeRemovedIndex).friendArrayList.contains(user)) {
			// remove the user from the friend's friend list
			masterUserList.get(friendToBeRemovedIndex).friendArrayList.remove(user);
			System.out.println(user + " has been removed from " + friendToBeRemoved + "'s friend list. :o)");
		} 
		else {
			System.out.println(user + " is not in " + friendToBeRemoved + "'s friend list. :o(");
		}

		//
		masterFriendList.get(userIndex).remove(friendToBeRemoved);
		masterFriendList.get(friendToBeRemovedIndex).remove(user);
		friendGraph = updateFriendGraph();
		partnerGraph = updatePartnerGraph();
		t.put(user, updateDescription(masterUserList.get(userIndex)));
		t.put(friendToBeRemoved, updateDescription(masterUserList.get(friendToBeRemovedIndex)));
		//remove partner
		removePartner(user, friendToBeRemoved);
	}

	public static void removePartner(String user, String partnerToBeAdded) {
		// get index for the user
		int userIndex = findIndex(user);
		//get index for the partner to be added
		int partnerToBeAddedIndex = findIndex(partnerToBeAdded);

		// if the user or the partner to be added doesn't exist, exit
		if (userIndex == -1 || partnerToBeAddedIndex == -1) {
			System.out.println("One of those people doesn't exist. :o( ");
			return;
		}

		// if the partner is in th users's friend list, remove
		if (masterUserList.get(userIndex).friendArrayList.contains(partnerToBeAdded)) {
			// remove the partner from the user's friend list
			masterUserList.get(userIndex).partnerArrayList.remove(partnerToBeAdded);
			System.out.println(partnerToBeAdded + " has been removed from " + user + "'s partner list. :o)");
		} else {
			System.out.println(partnerToBeAdded + " is not in " + user + "'s partner list. :o(");
		}

		// if the user is in the partner's friend list, remove
		if (masterUserList.get(partnerToBeAddedIndex).friendArrayList.contains(user)) {
			// remove the user from the partner's friend list
			masterUserList.get(partnerToBeAddedIndex).partnerArrayList.remove(user);
			System.out.println(user + " has been removed from " + partnerToBeAdded + "'s partner list. :o)");
		} else {
			System.out.println(user + " is not in " + partnerToBeAdded + "'s partner list. :o(");
		}
		masterPartnerList.get(userIndex).remove(partnerToBeAdded);
		masterPartnerList.get(partnerToBeAddedIndex).remove(user);
		friendGraph = updateFriendGraph();
		partnerGraph = updatePartnerGraph();
		t.put(user, updateDescription(masterUserList.get(userIndex)));
		t.put(partnerToBeAdded, updateDescription(masterUserList.get(partnerToBeAddedIndex)));

	}

	public static Graph updateFriendGraph(){
		Graph updatedFriendGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String friend : masterFriendList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (friend.equals(masterUserList.get(j).username)) {
						updatedFriendGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}
		return(updatedFriendGraph);
	}
	
	public static Graph updatePartnerGraph(){
		Graph partnerGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String partner : masterFriendList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (partner.equals(masterUserList.get(j).username)) {
						partnerGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}
		return(partnerGraph);
	}
	
	public static String updateDescription(UserInfo info) {
		info.description = " " + (info.isPolyamorous == true ? "\n" + "This user is polyamorous." + "\n"
				: "\n" + "This user is not polyamorous" + "\n");
		if (info.friendArrayList.isEmpty()) {
			info.description += "\n" + info.username + " has no friends." + "\n";
		} else {
			info.description += "\n" + info.username + " has the following friends" + "\n";
			for (String friend : info.friendArrayList) {
				info.description += friend + "\n";
			}
		}
		if (info.partnerArrayList.isEmpty()) {
			info.description += "\n" + info.username + " has no partners." + "\n";
		}

		else if (info.isPolyamorous = true) {
			info.description += "\n" + info.username + " has the following partners: " + "\n";
			for (String partner : info.partnerArrayList) {
				info.description += partner + "\n";
			}
		} else {
			info.description += "\n" + info.username + " has the following partner:" + "\n";
			for (String partner : info.partnerArrayList) {
				info.description += partner + "\n";
			}
		}

		info.description += "========================================";
		return (info.description);
	}

	public static String addUser(String username, String basicDescription, String friends, String partners, boolean isPolyamorous) {
		int usernameLength = username.length();
		String description = "========================================" + "\n" + username + "\n";
		for (int i = 0; i < username.length(); i++) {
			description += ("-");
		}
		description += "\n" + basicDescription;

		String[] friendString = friends.split(", ");
		String[] partnerString = partners.split(", ");

		ArrayList<String> friendArrayList = new ArrayList<String>();
		ArrayList<String> partnerArrayList = new ArrayList<String>();

		for (int i = 0; i < friendString.length; i++) {
			friendArrayList.add(friendString[i]);
		}
		for (int j = 0; j < partnerString.length; j++) {
			partnerArrayList.add(partnerString[j]);
		}

		UserInfo info = new UserInfo(username, description, isPolyamorous, friends, partners, friendArrayList,
				partnerArrayList, numUsers);

		 description += updateDescription(info);

		t.put(username, description);

		masterFriendList.add(friendArrayList);
		masterPartnerList.add(partnerArrayList);
		masterUserList.add(info);
		numUsers++;

		Graph friendGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String friend : masterFriendList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (friend.equals(masterUserList.get(j).username)) {
						friendGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}

		Graph partnerGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String partner : masterFriendList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (partner.equals(masterUserList.get(j).username)) {
						partnerGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}
		return (username + " has been added to FaceSpace.");
	}

	public static String searchUser(String username) {
		return t.get(username);
	}

	public static int shortestPaths(Graph g, int source, int target) {
		BreadthFirstPaths paths = new BreadthFirstPaths(g,source);
        for (int v = 1; v < g.numVertices() ; v++) {
        	if(paths.pathTo(v) == null){
        		v++;
        	}
        	if(paths.pathTo(v).elementAt(0).equals(target)){
                return(paths.pathTo(v).size() - 1);
        	}
        }
        return(-1);
	}

	public static void main(String[] args) throws FileNotFoundException {

		numUsers = 0;
		t = new RedBlackBST();
		Scanner scan = new Scanner(new FileInputStream(args[0]));
		masterUserList = new ArrayList<UserInfo>();
		masterFriendList = new ArrayList<ArrayList<String>>();
		masterPartnerList = new ArrayList<ArrayList<String>>();

		while (scan.hasNext()) {
			String line = scan.nextLine();
			String[] fields = line.split("/");
			String username = fields[0].trim();
			int usernameLength = username.length();
			String description = "========================================" + "\n" + username + "\n";
			for (int i = 0; i < username.length(); i++) {
				description += ("-");
			}
			description += "\n" + fields[1].trim();
			String friends = fields[2].trim();
			String partners = fields[3].trim();
			boolean isPolyamorous = (fields[4].trim().equalsIgnoreCase("y") ? true : false);

			String[] friendString = friends.split(", ");
			String[] partnerString = partners.split(", ");

			ArrayList<String> friendArrayList = new ArrayList<String>();
			ArrayList<String> partnerArrayList = new ArrayList<String>();

			for (int i = 0; i < friendString.length; i++) {
				friendArrayList.add(friendString[i]);
			}
			for (int j = 0; j < partnerString.length; j++) {
				partnerArrayList.add(partnerString[j]);
			}
			description += " " + (isPolyamorous == true ? "\n" + "This user is polyamorous." + "\n"
					: "\n" + "This user is not polyamorous" + "\n");
			if (friends.isEmpty()) {
				description += "\n" + username + " has no friends." + "\n";
			} else {
				description += "\n" + username + " has the following friends" + "\n";
				for (String friend : friendArrayList) {
					description += friend + "\n";
				}
			}
			if (partners.isEmpty()) {
				description += "\n" + username + " has no partners." + "\n";
			}

			else if (isPolyamorous = true) {
				description += "\n" + username + " has the following partners: " + "\n";
				for (String partner : partnerArrayList) {
					description += partner + "\n";
				}
			} else {
				description += "\n" + username + " has the following partner:" + "\n";
				for (String partner : partnerArrayList) {
					description += partner + "\n";
				}
			}

			description += "========================================";

			t.put(username, description);

			UserInfo info = new UserInfo(username, description, isPolyamorous, friends, partners, friendArrayList,
					partnerArrayList, numUsers);

			masterFriendList.add(friendArrayList);
			masterPartnerList.add(partnerArrayList);
			masterUserList.add(info);
			numUsers++;
		}

		friendGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String friend : masterFriendList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (friend.equals(masterUserList.get(j).username)) {
						friendGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}

		partnerGraph = new Graph(numUsers);
		for (int i = 0; i < masterUserList.size(); i++) {
			for (String partner : masterPartnerList.get(i)) {
				for (int j = 0; j < masterUserList.size(); j++) {
					if (partner.equals(masterUserList.get(j).username)) {
						partnerGraph.addEdge(i, masterUserList.get(j).reference);
					}
				}
			}
		}
				
		System.out.println("=============  Welcome to FaceSpace! :o) =============");
		System.out.println("                            /  Ramos  \\");
		System.out.println("               \u00A9 2017   E. |   Rexer   |");
		System.out.println("                            \\  Eckels /");
		System.out.println("................. " + numUsers + " users worldwide " + ".................");
		while (true) {
			boolean isNameTaken = false;
			System.out.println("\n" + "Main Menu");
			System.out.println("\t" + "\"a\" to add a user");
			System.out.println("\t" + "\"s\" to search all users");
			System.out.println("\t" + "\"u\" to update a user's description");
			System.out.println("\t" + "\"addF\" to add a friend to a user's profile");
			System.out.println("\t" + "\"addP\" to add a partner to a user's profile");
			System.out.println("\t" + "\"rF\" to remove a friend from a user's profile");
			System.out.println("\t" + "\"rP\" to remove a partner from a user's profile");
			System.out.println("\t" + "\"bfs\" to search for degrees of separation");
			System.out.println("\t" +  "\"q\" to quit.");
			Scanner scanner = new Scanner(System.in);
			String next = scanner.next();
			if (next.equalsIgnoreCase("a")) {
				Scanner addScanner = new Scanner(System.in);
				System.out.println("Enter a user to be added:");
				System.out.println("Username:");
				String username = addScanner.nextLine();
				for (int i = 0; i < masterUserList.size(); i++) {
					if (username.equals(masterUserList.get(i).username)) {
						isNameTaken = true;
						System.out.println("This username is already taken!");
						System.out.println("How about " + username + (int) (10 * Math.random()) + "?");
					}
				}
				if (!isNameTaken) {
					System.out.println("Description:");
					String description = addScanner.nextLine();
					System.out.println("Friends, separated by a comma (leave blank if none): ");
					String friends = addScanner.nextLine();
					System.out.println("Partners, separated by a comma (leave blank if none): ");
					String partners = addScanner.nextLine();
					System.out.println("Is this user polyamorous? \"y\" or \"n\". ");
					boolean isPolyamorous = (addScanner.nextLine().equalsIgnoreCase("y") ? true : false);
					System.out.println();
					System.out.println(addUser(username, description, friends, partners, isPolyamorous));
				}
			} 
			else if (next.equalsIgnoreCase("s")) {
				System.out.println("Enter a username:");
				Scanner searchScanner = new Scanner(System.in);
				String enteredName = searchScanner.nextLine();
				System.out.println();
				if (searchUser(enteredName) == null) {
					System.out.println("User not found.");
					for (int i = 0; i < masterUserList.size(); i++) {
						if (enteredName.equalsIgnoreCase(masterUserList.get(i).username)) {
							System.out.println("Did you mean " + masterUserList.get(i).username + "?");
						}
					}
				}
				else {
					System.out.println(searchUser(enteredName));
				}
			} else if (next.equalsIgnoreCase("u")) {
				System.out.println("Which user would you like to update?");
				Scanner rfScanner = new Scanner(System.in);
				String username = rfScanner.nextLine();
				System.out.println("Enter the new description.");
				String description = rfScanner.nextLine();
				updateUser(username, description);
			} else if (next.equalsIgnoreCase("rF")) {
				System.out.println("Enter the user and the friend to be removed, separated by a comma and a space.");
				Scanner rfScanner = new Scanner(System.in);
				String friends[] = rfScanner.nextLine().split(", ");
				String friend0 = friends[0];
				String friend1 = friends[1];
				removeFriend(friend0, friend1);

			} else if (next.equalsIgnoreCase("rP")) {
				System.out.println("Enter the user and the partner to be removed, separated by a comma and a space.");
				Scanner rfScanner = new Scanner(System.in);
				String friends[] = rfScanner.nextLine().split(", ");
				String friend0 = friends[0];
				String friend1 = friends[1];
				removePartner(friend0, friend1);
			} else if (next.equalsIgnoreCase("addF")) {
				System.out.println("Enter the user and the friend to be added, separated by a comma and a space.");
				Scanner rfScanner = new Scanner(System.in);
				String[] friends = rfScanner.nextLine().split(", ");
				if(friends.length != 2){
					System.out.println("Invalid input!");
				}
				else {
					String friend0 = friends[0];
					String friend1 = friends[1];
					addFriend(friend0, friend1);
				}
			} else if (next.equalsIgnoreCase("addP")) {
				System.out.println("Enter the user and the partner to be added, separated by a comma and a space.");
				Scanner rfScanner = new Scanner(System.in);
				String partners[] = rfScanner.nextLine().split(", ");
				String partner0 = partners[0];
				String partner1 = partners[1];
				addPartner(partner0, partner1);
			} 
			else if(next.equalsIgnoreCase("bfs")){
				
				System.out.println("Enter friends or partners");
				Scanner scanner2 = new Scanner(System.in);
				String friends = scanner2.nextLine();
				System.out.println("Enter source");
				String sourceString = scanner2.nextLine();
				System.out.println("Enter target");
				String targetString = scanner2.nextLine();
				int source = findIndex(sourceString);
				int target = findIndex(targetString);
				
				if(friends.equals("friends")){
					System.out.println(friendGraph);
					System.out.println(shortestPaths(friendGraph, source, target) + " degrees of separation");
				}
				else{
					System.out.println(partnerGraph);
					System.out.println(shortestPaths(partnerGraph, source, target) + " degrees of separation");
				}
			}
			else if (next.equals("q")) {
				return;
			}
		}
	}
}
