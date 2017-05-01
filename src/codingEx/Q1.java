package codingEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Q1 {
    static class Interval {
        int start;
        int end;
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
	/*
	*The algorithm first sorts all the intervals in descending order of their start times.
	*Next, it merges overlapping intervals among the sorted intervals in-place.
	*Finally, it iterates through the sorted,merged list to find the uncovered portions.
	*Run Time: Sorting: O(nlogn), Merging: O(n), Find Uncovered Portion: O(n)
	*Total Run Time: O(nlogn).
	*/
    private static List<Interval> uncoveredIntervals(List<Q1.Interval> intervals) {
        List<Interval> uncovered = new ArrayList<Q1.Interval>();
        if(intervals==null)
        	return null;
        //Sort the intervals in the decreasing order.
        Collections.sort(intervals, new Comparator<Q1.Interval>(){
            public int compare(Interval i1, Interval i2){
                if(i1.start!=i2.start)
                    return i2.start-i1.start;
                else
                    return i2.end-i1.end;
            }
        });
        //Merge intervals that are overlapping.
        int index = 0;
        for (int i=0; i<intervals.size(); i++)
        {
            
            if (index != 0 && intervals.get(index-1).start <= intervals.get(i).end)
            {
                while (index != 0 && intervals.get(index-1).start <= intervals.get(i).end)
                {
                	intervals.get(index-1).end = Math.max(intervals.get(index-1).end, intervals.get(i).end);
                	intervals.get(index-1).start = Math.min(intervals.get(index-1).start, intervals.get(i).start);
                    index--;
                }
            }
            else {
            	intervals.set(index,intervals.get(i));
            }
     
            index++;
        }
        // Find if there is a gap in between merged intervals. If so add to the uncovered list. Return uncovered List.
        Q1.Interval prevInterval = intervals.get(0);
        for(int i=1;i<intervals.size();i++) {
        	Q1.Interval currInterval = intervals.get(i);
        	if(currInterval.start>prevInterval.start)
        		break;
        	if(currInterval.end<prevInterval.start){
        		Q1.Interval newInterval = new Interval(currInterval.end,prevInterval.start);
        		uncovered.add(newInterval);
        	}
        	prevInterval = currInterval;
        		
        }
        return uncovered;
    }

    /*
     *  Hey! You probably don't need to edit anything below here
     */

    private static List<Q1.Interval> readIntervals(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Q1.Interval> intervals = new ArrayList<Q1.Interval>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            intervals.add(toInterval(line));
        }
        return intervals;
    }

    private static Q1.Interval toInterval(String line) {
        final String[] tokenizedInterval = line.split(" ");

        return new Interval(Integer.valueOf(tokenizedInterval[0]),
                            Integer.valueOf(tokenizedInterval[1]));
    }

    public static void main(String... args) throws IOException {
        //List<Q1.Interval> intervals = Q1.readIntervals(System.in);
List<Q1.Interval> intervals = new ArrayList<Interval>();
intervals.add(new Interval(2,6));intervals.add(new Interval(9, 12));intervals.add(new Interval(8, 9));
intervals.add(new Interval(18, 21));intervals.add(new Interval(4,7));intervals.add(new Interval(10, 11));
        List<Q1.Interval> uncovered = Q1.uncoveredIntervals(intervals);
        for (Interval i : uncovered) {
            System.out.println(i.start + " " + i.end);
        }
    }
}
