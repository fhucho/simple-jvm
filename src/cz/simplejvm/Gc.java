package cz.simplejvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.simplejvm.Heap.HeapObject;
import cz.simplejvm.StackFrame.Reference;

public class Gc {
	public enum Color {
		WHITE, GRAY, BLACK
	}

	public static void gc(Heap heap, List<StackFrame> stackFrames) {
		// Mark GC roots as gray.
		Set<Reference> graySet = new HashSet<Reference>();
		for (StackFrame sf : stackFrames) {
			graySet.addAll(sf.getGcRoots());
		}
		for (Reference ref : graySet) {
			heap.getObject(ref).setColor(Color.GRAY);
		}

		// Process gray objects.
		while (!graySet.isEmpty()) {
			Reference ref = graySet.iterator().next();
			graySet.remove(ref);
			HeapObject obj = heap.getObject(ref);
			obj.setColor(Color.BLACK);
			obj.grayAllWhiteReferences(heap, graySet);
		}

		// Keep only black objects and recolor them to white.
		Map<Reference, Reference> refMap = new HashMap<Reference, Reference>();
		List<HeapObject> src = heap.getObjects();
		List<HeapObject> dst = new ArrayList<HeapObject>();
		for (int i = 0; i < src.size(); i++) {
			HeapObject obj = src.get(i);
			if (obj.getColor() == Color.BLACK) {
				obj.setColor(Color.WHITE);
				dst.add(obj);
				refMap.put(obj.getReference(), new Reference(dst.size() - 1));
			}
		}
		src.clear();
		src.addAll(dst);

		// Update references.
		heap.updateReferences(refMap);
		for(StackFrame sf:stackFrames) {
			sf.updateReferences(refMap);
		}
	}
}
