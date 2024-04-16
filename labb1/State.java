package labb1;

import java.util.*;

class State extends GlobalSimulation {

	public int numberInQueueA = 0, numberInQueueB = 0, accumulated = 0, noMeasurements = 0;

	private EventList myEventList;

	Random slump = new Random();

	State(EventList x) {
		myEventList = x;
	}

	private void InsertEvent(int event, double timeOfEvent) {
		myEventList.InsertEvent(event, timeOfEvent);
	}

	public void TreatEvent(Event x) {
		switch (x.eventType) {
		case ARRIVAL_A:
			arrivalA();
			break;
		case ARRIVAL_B:
			arrivalB();
			break;
		case READY_A:
			readyA();
			break;
		case READY_B:
			readyB();
			break;
		case MEASURE:
			measure();
			break;
		}
	}

	private double generateMean(double mean) {
		return 2 * mean * slump.nextDouble();
	}

	private void arrivalA() {
		if (numberInQueueB + numberInQueueA == 0) {
			InsertEvent(READY_A, time + 0.002);
		}
		numberInQueueA++;
		InsertEvent(ARRIVAL_A, time + generateMean(1.0 / 150));
	}

	private void arrivalB() {
		if (numberInQueueB + numberInQueueA == 0) {
			InsertEvent(READY_B, time + 0.004);
		}
		numberInQueueB++;
	}

	private void readyA() {
		numberInQueueA--;
		if (numberInQueueB > 0) {
			InsertEvent(READY_B, 0.004);
		} else if (numberInQueueA > 0) {
			InsertEvent(READY_A, time + 0.002);
		}
		InsertEvent(ARRIVAL_B, time + 1);
	}

	private void readyB() {

		numberInQueueB--;
		if (numberInQueueB > 0) {
			InsertEvent(READY_B, time + 0.004);
		} else if (numberInQueueA > 0) {
			InsertEvent(READY_A, 0.002);
		}
	}

	private void measure() {
		accumulated = accumulated + numberInQueueA + numberInQueueB;
		noMeasurements++;
		InsertEvent(MEASURE, time + 0.1);
	}
}