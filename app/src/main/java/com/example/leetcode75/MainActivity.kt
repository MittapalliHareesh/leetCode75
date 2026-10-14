package com.example.leetcode75

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.leetcode75.ui.theme.LeetCode75Theme
import java.util.LinkedList
import java.util.Queue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeetCode75Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

fun leetCodeProblem(): String {
    val str1 = "ABCAABCAABCA"
    val str2 = "ABCAABCA"
    //Brute Force way

    val i = minOf(str2.length, str1.length)

    // i in i ..1 won't work ..should be used only incremental scenario
    // here we should use downTo for decremental secnario.
    for (i in i downTo 1) {
        if (valid(str1, str2, i))
            return str1.substring(0, i)
    }
    return ""
}

fun valid(str1: String, str2: String, index: Int): Boolean {
    if (str1.length % index > 0 || str2.length % index > 0) {
        return false
    } else {
        val baseString = str2.substring(0, index)
        return str1.replace(baseString, "").isEmpty() && str2.replace(baseString, "").isEmpty()
    }
}

fun optimizedWay(): String {
    val str1 = "ABCAABCAABCA"
    val str2 = "ABCAABCA"

    if ((str2 + str1) != str1 + str2) {
        return ""
    }

    val gcdLength = gcd(str1.length, str2.length)
    return str1.substring(0, gcdLength)
}

fun gcd(firstStringLength: Int, secondStringLength: Int): Int {
    return if (secondStringLength == 0) {
        firstStringLength
    } else {
        gcd(secondStringLength, firstStringLength % secondStringLength)
    }
}


//LeetCode 605. Can Place Flowers in alternate positions.
class Problem605 {
    //input1 [0,0,1,0,1]
    //input2 [1,0,0,0,1]
    fun canPlaceFlowers(flowerbed: IntArray, n: Int): Boolean {
        var counter = 0
        for (i in flowerbed.indices) {
            if (flowerbed[i] == 0) {
                val isLeftEmpty = i == 0 || flowerbed[i - 1] == 0
                val isRightEmpty = i == flowerbed.size - 1 || flowerbed[i + 1] == 0

                if (isLeftEmpty && isRightEmpty) {
                    flowerbed[i] = 1
                    counter++
                }
            }
        }
        return counter >= n
    }
}

//LeetCode345. Reverse Vowels of a String.
class Problem345 {
    //input1  IceCreAm  ==> AceCreIm
    //input2  leetcode  ==> leotcede
    fun reverseVowels(s: String): String {
        val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
        val chrs = s.toCharArray()
        var left = 0
        var right = s.lastIndex

        while (left < right) {
            while (left < right && chrs[left] !in vowels) left++
            while (left < right && chrs[right] !in vowels) right++
            if (left < right) {
                //Here without using extra temp variable.
                chrs[left] = chrs[right].also { chrs[right] = chrs[left] }
                left++
                right--
            }
        }
        return String(chrs)
    }
}

////////////////////////////////////////******************** GRAPHS ********************//////////////////////////////////////////
/*
* There are 3 ways we can create
*   Adjacent matrix == Here we create a 2*2 matrix and Node 1,2 connection is there then we make that as 1 else 0
*   Adjacent List  == Its uses HashMap<NodeValue, ListOfEdges>. If Node 1 to 2, 1 to 3 edges are there i.e map will have key has 1 value is list(2,3)
*   Edge List == It's a like Pair. If Node 1 to 2, 1 to 3 edges are there then we do like (1,2) (1,3) ...
*
*   Always Prefer Adjacent List approach only which is HashMap.
*
*   And there are two types of graph's. Directed and Undirectional graphs. Directed graph will be having arrows. Undirected
*   graph doesn't have arrows. In case of Undirected graph Node 1 to 2 edge connection is there i.e automatically 2 to 1 also there.
* */

fun graphCreation() {
    //Here I want to create a UnDirected graph. And form edges b/w those nodes.

    //First will be implementing Adjacent List approach that means using hashMap.
    val adjacentList = HashMap<Int, ArrayList<Int>>()

    //Form edges here. Like from Node 1 to 2, 1 to 3, 1 to 4, 2 to 3, 3 to 5, 4 to 5.
    addEdge(1, 2, adjacentList)
    addEdge(1, 3, adjacentList)
    addEdge(1, 4, adjacentList)
    addEdge(2, 3, adjacentList)
    addEdge(3, 5, adjacentList)
    addEdge(4, 5, adjacentList)

    //Now get ConnectedNodes for each node.
    val connectedNodesList = getConnectedNodes(1, adjacentList)

    println(adjacentList)
}

fun addEdge(source: Int, destination: Int, adjacentList: HashMap<Int, ArrayList<Int>>) {
    //Here very first we have to see value List object created or not. If not create it.
    //And as mentioned we are creating UnDi  rected graph so, source to destination and same time destination to source have edges.
    adjacentList.putIfAbsent(source, arrayListOf())
    adjacentList.putIfAbsent(destination, arrayListOf())

    adjacentList[source]!!.add(destination)
    adjacentList[destination]!!.add(source)
}

fun getConnectedNodes(nodeNumber: Int, adjacentList: HashMap<Int, ArrayList<Int>>) =
    adjacentList.getOrDefault(nodeNumber, arrayListOf())


/*
* Now to traverse all Nodes through the Graph we have 2 ways.
*
*    Breadth first Search (BFS) / Level traversal Search.
*    Depth first Search (DFS)
*
*    Imp:- Either BFS OR DFS we should have sourceNode i.e where to start. Based on that output changes.
*
*    In general in graph we say BFS, In Tree we say LTS. Both are same. This case always we have to use QUEUE data structure.
*    Example:- Node 1 has 2, 3 edges. Again 2 to 4 , 3 to 5.
*      That means Node 1 is level 0 &
*                 Node 2, Node 3 are level 1
*                 Node 4, Node 5 are level 2.
*
*    Always we traverse 1 edge only,  Whenever we traverse from 1 to 2 or 1 to 3. Now lets say we are Node 2 from this
*    we have to traverse next nodes which is 1 edge difference. At that time 2 to 4 && 2 to 1 also comes in to picture
*    because ours is Undirected graph. So again went 1 from went 2 ... like this loop happens.
*    That's why use extra data structure called Set. Here maintain visited Nodes so that loop won't occur.
* */

//Here source means startingNode where to start.
fun bfsTraversal(source: Int, adjacentList: HashMap<Int, ArrayList<Int>>) {
    val queue = LinkedList<Int>() //Here LinkedList extends queue.
    val visitedNodes = HashSet<Int>() // To maintain visitedNodes list.

    //Step 1:- Very first time insert source into queue and visitedNodes set.
    queue.offer(source)
    visitedNodes.add(source)

    //Step 2:- If Queue not empty pop element print that and find adjacent nodes for that and
    //add to queue and set (if it's not in set) and repeat process until queue is empty.
    while (queue.isNotEmpty()) {
        val nodeNumber = queue.poll() as Int
        println(nodeNumber)

        val adjacentNodesList = getConnectedNodes(nodeNumber, adjacentList)
        //Iterate over each node in list and it in queue and set.
        for (i in adjacentNodesList) {
            if (!visitedNodes.contains(i)) {
                queue.offer(i)
                visitedNodes.add(i)
            }
        }
    }
}

/*
* In DFS (depth first search) as name says from source Node to till depth we go here. where as FS we go level wise
*  Like 1 connected 2, 3.
*  2 connected to 4, 5
*  4 connected 3, 5
*  Will start at 1 and then 2 from 2 will go to 4 or 5 whichever comes first, Lets say 4.
*       From 4 we go to 3 or 5 whichever comes first lets say 3
*       from 3 no nodes connected to back to previous step Like from we picked 3 now will pick 5 ...like this.
*
*  here in subNode it's our wish to pick firstone or secondone. Based on that response will change
*
*  For Tree of Graph DFS we will use recursion. In genereal tree's / graph's using recursion will first cover left side and
*  then go right side.
*
*  In general Trees has left or right only two nodes. But graphs doesn't like that.
*  In graph 1 to 2,3 -- 2 will be connected to 3, 4, 5 as well. Now 3 will be connected to 2,4,5, even 1 also if u want.
* */

//Here source means startingNode where to start.
fun dfsTraversal(
    source: Int,
    visitedSet: HashSet<Int>,
    adjacentList: HashMap<Int, ArrayList<Int>>
) {
    println(source)

    //Step1:- Add it to visitedSet.
    visitedSet.add(source)

    //Step2:- Fetch connected nodes for this node.
    val adjacentNodesList = getConnectedNodes(source, adjacentList)

    //Step3:- Iterate over each node in adjacentNodesList and traverse through that.
    for (node in adjacentNodesList) {
        if (!visitedSet.contains(node)) {
            dfsTraversal(
                node,
                visitedSet,
                adjacentList
            ) //That's all. Here we are writing recursion.
        }
    }
}
/////////////////////////////////// Below will see how we can do BFS / DFS for Disconnected graph.
/**
 * Disconnected graph means there is no edge connection b/w some nodes.
 *  Node 1 -->2, 3
 *  2 --> 3, 5
 *  6 --> 7,9
 *  7 --> 6,9
 *
 *  Here if you observe Nodes 1,2,3 and 5 somehow connected. To this Node 6 not connected again 6 connected to 7 and 9
 *  But no edge connection b/w 5 to 6 ---> This is called disconnected graph.
 *
 *  How we will implement BFS / DFS for this graphs will see below.
 *
 *
 *  Small change only, while calling dfs/bfs from main fun create a loop with i= 1 to 10 and iterate over all nodes
 *  before that add visitedNodes check. If node not visited then call this fun.
 *
 *  Below Leet code problem is example.
 */

////---/////////---////////---////////---////////---////////---////////---////////---////////---////////---////////---////////---////////---////

/*
* Leet code  547. Number of Provinces
* Here inout given in adjacent matrix that we are converting to adjacentList (Using HashMap) and creating a graph then will do BFS /DFS.
*
* Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]] ....       1 1 0        example index starts with 1. i.e means here Node 1 connected to self & Node 1 connected to 2.
*                                                           1 1 0        Node 2 connected Node 1 and its self.
*                                                           0 0 1        Node 3 connected its self. Here Node 1 2 & Node 3  two graphs exists, that's the output.
* Output: 2
* */

//Since it's a disconnected graph, so firstNode to secondNode and secondNode to  firstNode we have to create edge connection.

//Solution 1 :- Converting adjacent matrix to list (using hashMap creating a graph) is a tedious approach.
class Solution {
    class Graph {
        val adjacentList = HashMap<Int, ArrayList<Int>>()

        fun addEdge(startNode: Int, endNode: Int) {
            adjacentList.putIfAbsent(startNode, ArrayList())
            adjacentList.putIfAbsent(endNode, ArrayList())

            adjacentList[startNode]!!.add(endNode)
            adjacentList[endNode]!!.add(startNode)
        }

        fun listOfGetConnectedNodes(node: Int) =
            adjacentList.getOrDefault(node, ArrayList())
    }

    fun findCircleNum(isConnected: Array<IntArray>): Int {
        val n = isConnected.size
        var count = 0
        //Here we are creating a graph with adjacent List way.
        val graph = Graph()
        for (i in 0 until n) {
            for (j in isConnected[i].indices) {
                if (isConnected[i][j] == 1) {
                    graph.addEdge(i, j)
                }
            }
        }

        //So here, we are iterating over entering matrices so already visitedNodes may come hence add that check.
        val visitedNodesSet = HashSet<Int>()
        for (i in 0 until n) {
            if (!visitedNodesSet.contains(i)) {
                //If 1st time this fun called then it iterates in depth of all Nodes and cover entire graph unless there is any disconnection in graph.
                //If disconnection then again this fun calls. That means how many times this fun executed that many provinces are there, that's the answer.
                dfsTraversalLeetCode(i, graph, visitedNodesSet)
                count++
            }
        }
        return count
    }

    private fun dfsTraversalLeetCode(i: Int, graph: Graph, visitedNodesSet: HashSet<Int>) {
        visitedNodesSet.add(i)
        val getConnectedNodes = graph.listOfGetConnectedNodes(i)
        for (connectedNode in getConnectedNodes) {
            if (!visitedNodesSet.contains(connectedNode)) {
                dfsTraversalLeetCode(connectedNode, graph, visitedNodesSet)
            }
        }
    }
}

//Solution 2:- Directly using adjacentMatrix only write dfs.
class Solution2 {

    // Your exact input matrix
    val isConnected = arrayOf(
        intArrayOf(1, 0, 0, 1),
        intArrayOf(0, 1, 1, 0),
        intArrayOf(0, 1, 1, 1),
        intArrayOf(1, 0, 1, 1)
    )
    val result = findCircleNum(isConnected)
    val n = isConnected.size

    //since we are playing with matrix so will take BooleanArray of nxn
    val visitedBooleanArray = BooleanArray(n)

    fun findCircleNum(isConnected: Array<IntArray>): Int {
        var provinceCount = 0
        for (i in 0 until n) {
            if (!visitedBooleanArray[i]) {
                matrixDfsTraversal(i)
                provinceCount++
            }
        }
        return provinceCount
    }

    fun matrixDfsTraversal(node: Int) {
        visitedBooleanArray[node] = true
        //Loop through potential neighbor nodes.
        for (j in isConnected.indices) {
            if (isConnected[node][j] == 1 && !visitedBooleanArray[j]) {
                matrixDfsTraversal(j)
            }
        }
    }
}


/*
* LeetCode 151. Reverse Words in a String
* */
fun leetCode151_reverseWords(s: String): String {
    //input is  "Kotlin is a  fun language  "
    val currentWord = StringBuilder()
    val wordsList = mutableListOf<String>()

    for (i in s.indices) {
        val char = s[i]
        if (char != ' ') {
            currentWord.append(char)
        } else if (currentWord.isNotEmpty()) {
            wordsList.add(currentWord.toString())
            currentWord.clear()
        }
    }

    if (currentWord.isNotEmpty()) {
        wordsList.add(currentWord.toString())
    }

    val result = StringBuilder()
    for (i in wordsList.size - 1 downTo 0) {
        result.append(i)
        if (i > 0) {
            result.append(" ")
        }
    }
    return result.toString()
}

////Other simple approach using
fun reverseWords(input: String): String {
    //Input is  ==>   "  Kotlin  is   fun  "

    /*
    * trim() results in: "Kotlin  is   fun"
    * split() results in: ["Kotlin", "is", "fun"]
    * reversed() results in: ["fun", "is", "Kotlin"]
    * joinToString(" ") results in: "fun is Kotlin"
    * */

    /*
    * input =   "A  B"
    * If we are using Regex("\\s")  ==> ["A"," ", "B"]
    * If we are using Regex("\\s+")  ==> ["A","B"]
    *
    * If you want 2 spaces then  Regex("\\s{2}") --> Then this will take 2 more spaces if one space it will ignore.
    * If you want 2 spaces then  Regex("\\s{3,5}") --> B/w spaces are from 3 to 5 then it will split till there.
    * */

    return input.trim() //It trims first and last spaces
        .split(Regex("\\s+")) //
        .reversed()
        .joinToString(" ") //If you don't pass separator by default takes , "blue, is, sky, the"
}

/*
* LeetCode 238. Product of Array Except Self
* Input: nums = [1,2,3,4]
Output: [24,12,8,6]

* */

class leetCode_238 {
    //This approach works fine but throws Time Limit Exceeded if >100 size integer array.
    /*    fun productExpectSelf(nums: IntArray): IntArray {
            var result = 1
            val resultNums = IntArray(nums.size)
            for (i in nums.indices) {
                result = 1
                for (j in nums.indices) {
                    if (i!=j) {
                        result *= nums[j]
                    }
                }
                resultNums[i] = result
            }

            return resultNums
        }*/

    /*
         Here we are considering if nums input array consist of zero's.
         If one zero expect that index rest all indexes are zero.
         If two zeros then entire array is zero.
    * */
    fun alternateApproach2(nums: IntArray): IntArray {

        //This is imp, because below after num.count index would be at last place.
        val n = nums.size

        val result = IntArray(n)


        val zerosCount = nums.count { it == 0 }

        //If zero count is >=2
        if (zerosCount >= 2) {
            return result
        }

        //If zero count is ==1, Just find the zero Index position replace with all other indexes calculation.
        if (zerosCount == 1) {
            var totalNonZeroIndexesCount = 1
            var zeroIndexPosition = -1
            for (i in 0 until n) {
                if (nums[i] == 0) {
                    zeroIndexPosition = i
                } else {
                    totalNonZeroIndexesCount *= nums[i]
                }
            }
            result[zeroIndexPosition] = totalNonZeroIndexesCount
            return result
        }

        /**
         * Else where there is no zeros condition. Here we prepare left array & right array and last we multiply both.
         * Left Array:- [1,2,3,4]
         * Like index left[0] place default set to 1.
         * left[1] = left[0] * nums[i-1]
         * left[2] = left[1] * nums[i-1] (While calculating left[1] position it has only previous values. That's why for every position we multiply left & previous current one)
         */


        val leftArray = IntArray(n) { 1 }
        val rightArray = IntArray(n) { 1 }

        //Here we are calculating towards right side from current index
        leftArray[0] = 1
        for (i in 1 until n) { //i.e means till n-1
            leftArray[i] = leftArray[i - 1] * nums[i - 1]
        }

        //Here we are calculating towards left side from current index
        for (i in n - 2 downTo 0) {
            rightArray[i] = rightArray[n + 1] * nums[i + 1]
        }

        for (i in 0 until n) {
            result[i] = leftArray[i] * rightArray[i]
        }

        return result
    }
}

/*
* 334. Increasing Triplet Subsequence
*
* Input: nums = [2,1,5,0,4,6]
Output: true
Explanation: One of the valid triplet is indcies are (1, 4, 5), because nums[1] == 1 < nums[4] == 4 < nums[5] == 6.
*
* */
class LeetCode334 {
    //In Kotlin in case of any hard coded values initialization use like intArrayOf(10,30)  (Here i starts with small)
    //In case of Dynamic initialization use IntArray(5) {it*2}  or Just declare integer array like IntArray(5)
    val x = IntArray(5) { it * 3 } //IntArray(5)

    val nums = intArrayOf(20, 100, 10, 12, 5, 13)  //10, 100


    /* This is simple easy approach but in this approach if you want that exact 3 indices may return wrong results. In this case just boolean so its fine.*/
    fun increasingTriplet(nums: IntArray): Boolean {
        //A triplet we need at least 3 numbers.
        if (nums.size < 3) return false

        var first = Int.MAX_VALUE
        var second = Int.MAX_VALUE

        for (num in nums) {
            if (num <= first) {
                first = num
            } else if (num <= second) {
                second = num
            } else {
                return true
            }
        }
        return false
    }

    //Approach 2 with returning proper indices.
    fun increasingTripletApproach2(nums: IntArray): Boolean {
        val n = nums.size
        if (n < 3) return false

        // Store the minimum value seen so far from the left
        val minFromLeft = IntArray(n)
        minFromLeft[0] = nums[0]
        for (i in 1 until n) {
            minFromLeft[i] = minOf(minFromLeft[i - 1], nums[i])
        }

        // Store the maximum value seen so far from the right
        val maxFromRight = IntArray(n)
        maxFromRight[n - 1] = nums[n - 1]
        for (i in n - 2 downTo 0) {
            maxFromRight[i] = maxOf(maxFromRight[i + 1], nums[i])
        }

        // Find if any element acts as the middle element of a triplet
        for (i in 1 until n - 1) {
            if (nums[i] > minFromLeft[i - 1] && nums[i] < maxFromRight[i + 1]) {
                //So those 3 numbers are minFromLeft[i - 1], nums[i], maxFromRight[i + 1]
                return true
            }
        }

        return false
    }
}

/*
* Count Number of Pairs With Absolute Difference K
* Given an integer array nums and an integer k, return the number of pairs (i, j) where i < j such that |nums[i] - nums[j]| == k.
*
* Input: nums = [1,2,2,1], k = 1
Output: 4
Explanation: The pairs with an absolute difference of 1 are:
- [1,2,2,1]
- [1,2,2,1]
- [1,2,2,1]
- [1,2,2,1]
* */
class LeetCode2006 {
    /*
    * It's similar like two-sum problem, but here we have to return possible number of pairs,
    * while storing in hashmap key should be index and value should be list of integer values in given array.
    * */
    val nums = intArrayOf(9, 3, 1, 1, 4, 5, 4, 9, 5, 10)
    val k = 1  ///// Its like a+b = k  ==> i.e  a = k-b

    fun countKDifference(nums: IntArray, k: Int): Int {
        var count = 0
        val resultMap = hashMapOf<Int, List<Int>>()

        for (index in nums.indices) {
            val resultData = if (nums[index] > k) nums[index] - k else nums[index] + k
            if (resultMap.containsKey(resultData)) {
                //Here resultData is one index and other index Position resultMap[resultData]
                count += resultMap[resultData]!!.size //count = 1 for value 4, 2 for value 5, 3 for value 4, count bump to 5 for value 5, count bump to 7 for value 10
            }
            val result = resultMap.getOrPut(nums[index]) { mutableListOf() }
            (result as? MutableList)?.add(index)
            // (9, listOf(indexPosition = 0, 7)), (3, listOf(indexPosition = 1)), (1, listOf(2,3)), (4, listOf(4, 6)), (5, listOf(5,8)), (10, listOf(9))

            //result pairs = (3,4) ; (4,5), (6,5) ; (3,4); (5,4), (5,4) ; (10,9), (10,9)
        }
        return count
    }
}


//Basic two-sum. Sum of two numbers = target. Here we have to return indexes.
class TwoSum {
    val nums = intArrayOf(2, 5, 6, 7, 4)
    val target = 9
    fun sumOfTwoNumbersEqualsToTarget(): IntArray {
        val hashMap = hashMapOf<Int, Int>()

        for (index in nums.indices) {
            val result = target - nums[index]
            if (hashMap.containsKey(result)) {
                //Those two indices are
                return intArrayOf(index, hashMap[result]!!)
            }
            hashMap[nums[index]] = index
        }
        return intArrayOf()
    }
}

/// Leetcode  392. Is Subsequence
/*
*
* Example 1:

Input: s = "abc", t = "ahbgdcd"
Output: true
Example 2:

Input: s = "axc", t = "ahbgdc"
Output: false
*
* */
class Solution111 {
    fun isSubsequence(s: String, t: String): Boolean {

        if (s.length > t.length || s.isEmpty()) return false

        var sourcePointer = 0 // It specifies source pointer.

        for (index in t.indices) {
            if (s[sourcePointer] == t[index]) {
                sourcePointer++
            }

            if (sourcePointer >= s.length) {
                return true
            }
        }
        return false
    }
}

/*
* 11. Container With Most Water
*
* Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
* */
class Solution222 {
    fun maxArea(height: IntArray): Int {
        var maxArea = 0
        var currentArea = 0

        var left = 0
        var right = height.size - 1

        while (left < right) {
            currentArea = minOf(height[left], height[right]) * (right - left)
            maxArea = maxOf(maxArea, currentArea)

            if (height[left] <= height[right]) {
                left++
            } else if (height[left] > height[right]) {
                right--
            }
        }

        return maxArea
    }
}

/*
* 1679. Max Number of K-Sum Pairs
*
* Input: nums = [1,2,3,4], k = 5
Output: 2
* */
class Solution1679 {
    fun maxOperations(nums: IntArray, k: Int): Int { // 3,1,3,4,3  K=6
        //1) Sort the array
        nums.sort()

        //2) Take two pointers
        var left = 0
        var right = nums.size - 1

        var count = 0
        while (left < right) { // Here main thing is sorting the array. That is important.
            val sum = nums[left] + nums[right]
            when {
                sum < k -> left++
                sum > k -> right--
                else -> {
                    count++
                    left++
                    right--
                }
            }
        }
        return count
    }
}

/*
* 643. Maximum Average Subarray
* */
class Solution643 {
    fun findMaxAverage(nums: IntArray, k: Int): Double { // nums = [1,12,-5,-6,50,3], k = 4
        if (nums.size < k) return 0.0

        var currentSum = 0
        var maximumSum = 0

        //Step1: First find the sum of numbers from 0 to till k elements.
        for (i in 0 until k) {
            currentSum += nums[i]
        }
        maximumSum = currentSum

        //Step2 : Now remove 1st index element and add kth index.
        for (i in k until nums.size) {
            currentSum = currentSum + nums[i] - nums[i - k]
            if (maximumSum < currentSum) {
                maximumSum = currentSum
            }
        }

        //Step3: divide maximum sum by k now
        return (maximumSum / k).toDouble()
    }
}

class SolutionMergeIntervals {
    //Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
    //Output: [[1,6],[8,10],[15,18]]

    fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
        //Step 1
        if (intervals.isEmpty()) return arrayOf()

        //Sort based on first element. Because input may be like  [[4,7] [1,4]]
        intervals.sortBy { it[0] }

        //Return output.
        val mergedList = mutableListOf<IntArray>()

        var currentInterval = intervals[0]
        mergedList.add(currentInterval)

        //check for overlap
        for (i in 1 until intervals.size) {
            val nextInterval = intervals[i]

            if (nextInterval[0] <= currentInterval[1]) {
                currentInterval[1] = maxOf(currentInterval[1], nextInterval[1])
                //I hope this is not required.
                //mergedList[mergedList.lastIndex] = currentInterval
            } else {
                currentInterval = nextInterval
                mergedList.add(currentInterval)
            }
        }
        return mergedList.toTypedArray()
    }
}

//LeetCode 435
class NonOverlappingIntervals {
    // [[1,2],[2,3],[3,4],[1,3]]
    // [[1,2],[1,2],[1,2]]

    // We have to return how many overlap intervals if we remove then it will become non-overlapping one.
    // Input ==  [[1,2],[2,3],[3,4],[1,3]]  ---> Here if we remove [1,3] then it becomes non-overlapping one. Hence, count is 1

    fun nonOverlappingIntervals(intervals: Array<IntArray>): Int {
        //As mentioned in problem we have to return count of overlapping intervals which we are going to remove. Return that count.
        var count = 0

        if (intervals.isEmpty()) return 0

        //In case of input 1 intervals are not sorted. We have to sort those.
        intervals.sortBy { it[0] } // [[1,2],[1,3],[2,3],[3,4]]

        var currentInterval = intervals[0]

        for (i in 1 until intervals.size) {
            val nextInterval = intervals[i]
            if (currentInterval[1] > nextInterval[0]) { // Here [1,2] [2,3], [3,4] are accepted hence we shouldn't use >=
                currentInterval[1] = minOf(currentInterval[1], nextInterval[1])
                count++
            } else {
                currentInterval = nextInterval
            }
        }

        return count
    }
}

//LeetCode 452. Minimum Number of Arrows to Burst Balloons
/*
* 🧱 Understanding the SetupImagine a giant wall with balloons taped to it.
* The Balloons are Horizontal Bars: Even though they are called "spherical," you only care about their width left-to-right.
* Each balloon is given to you as a range [start, end] along the floor (the X-axis).
*
* Example: A balloon written as [2, 6] stretches horizontally from position 2 to position 6.
* The Arrows Fly Straight Up: You stand on the floor (the X-axis) and shoot arrows straight up into the ceiling.
* An arrow shot from position X = 5 will travel vertically in a straight line forever.
* It will pop every single balloon that happens to cover position 5.
*
*
* Balloons can overlap each other. Because arrows travel upward infinitely and can pop multiple balloons at once,
* you want to find the overlapping zones to line up your shots efficiently.
* Visualizing an Example:Imagine three balloons placed like this on a grid:text

Balloon A:   [1 -------- 6]
Balloon B:        [2 --------- 8]
Balloon C:                [7 -------- 12]
            -----------------------------> X-Axis
Position:    1    2      6   7   8      12
* */
class MinimumNumOfArrowsToBurstBalloons {

    /**
    * How we are solving is ... Input points = [[10,16],[2,8],[1,6],[7,12]]
    *
    * 1) First we have to sort based on 1st position.
    *       By sorting by the end coordinate, you are essentially saying:"Let's find the balloon that finishes first.
    *       Because it finishes first, we are forced to shoot an arrow at or before its end, otherwise we will miss it forever."
    *
    * After sorting data is   1,6  2,8  7,12  10,16
    *
    * 2) Try to shoot arrow always at end position of 1st one. That means at 6
    *    Now arrow position is at 6.
    *    and by default arrowCount is 1.
    *
    *
    *    for(1 to end) {
    *      if(next[0] i.e 2 > arrowPosition i.e 6) {
    *       // then increment arrowCount
    *       and set arrowPosition next[1] (next one end)
    *     }
    *    }
    *
    * */
    fun findMinArrowShots(points: Array<IntArray>) : Int {
        if (points.isEmpty()) return 0

        //Imp sort based on 1st position here. Other above overlap problems we sort 0th position.
        points.sortBy { it[1] }

        //Taking current arrow positon as 0th index 1st position
        // val currentArrowPosition = points[0][1] --- can be written like below as well in 2 lines.
        val currentBallon = points[0]
        var currentArrowPosition = currentBallon[1]

        //By default, minimum we need one arrow. So arrowCount starts with 1
        var arrowsCount = 1

        for (i in 1 until points.size) {
            val nextBallon = points[i]

            if (nextBallon[0] > currentArrowPosition) {
                arrowsCount++
                currentArrowPosition = nextBallon[1]
            }
        }

        return arrowsCount
    }
}