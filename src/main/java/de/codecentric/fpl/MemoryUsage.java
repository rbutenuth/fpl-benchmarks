package de.codecentric.fpl;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import de.codecentric.fpl.datatypes.list.FplList;
import de.codecentric.linked.list.SingleLinkedList;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
public class MemoryUsage {

	public static void main(String[] args) {
		System.out.println(VM.current().details());
		// System.out.println(ClassLayout.parseClass(FplList.class).toPrintable());
		
		int reportingPoint = 0;
		System.out.println("size;FplList;SingleLinkedList;Vector");
		FplList<Integer> fplList = (FplList<Integer>) FplList.EMPTY_LIST;
		SingleLinkedList<Integer> linkedList = (SingleLinkedList<Integer>) SingleLinkedList.EMPTY_LIST;
		Vector<Integer> vector = Vector$.MODULE$.empty();
		for (int i = 0; i <= 100000; i++) {
			if (i == reportingPoint) {
				System.out.println(i + ";" + GraphLayout.parseInstance(fplList).totalSize() + ";" + GraphLayout.parseInstance(linkedList).totalSize() + ";" + GraphLayout.parseInstance(vector).totalSize());
				reportingPoint = computeNextReportingPoint(reportingPoint);
			}
			fplList = fplList.addAtEnd(i);
			linkedList = linkedList.addAtStart(i);
			vector = (Vector<Integer>)vector.$plus$colon(i);
		}
		
	}

	private static int computeNextReportingPoint(int reportingPoint) {
		if (reportingPoint == 0) {
			return 1;
		} else {
			return 10 * reportingPoint;
		}
	}

}
