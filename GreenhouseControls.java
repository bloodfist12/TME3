//: innerclasses/GreenhouseControls.java
// This produces a specific application of the
// control system, all in a single class. Inner
// classes allow you to encapsulate different
// functionality for each type of event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct 21, 2005
 *
 */

import java.io.*;
import tme3.*;
import java.util.Scanner;

public class GreenhouseControls extends Controller {
  private int errorcode;
  private boolean light = false;
  private boolean water = false;
  private boolean fans = false;
  private String thermostat = "Day";
  private boolean windowok = true;
  private boolean poweron = true;
  private String eventsFile = "unspecified file name";


  public class LightOn extends Event {
    public LightOn(long delayTime) { super(delayTime); }
    public void action() {
      // Put hardware control code here to
      // physically turn on the light.
      light = true;
    }
    public String toString() { return "Light is on"; }
  }
  public class LightOff extends Event {
    public LightOff(long delayTime) { super(delayTime); }
    public void action() {
      // Put hardware control code here to
      // physically turn off the light.
      light = false;
    }
    public String toString() { return "Light is off"; }
  }
  public class WaterOn extends Event {
    public WaterOn(long delayTime) { super(delayTime); }
    public void action() {
      // Put hardware control code here.
      water = true;
    }
    public String toString() {
      return "Greenhouse water is on";
    }
  }
  public class WaterOff extends Event {
    public WaterOff(long delayTime) { super(delayTime); }
    public void action() {
      // Put hardware control code here.
      water = false;
    }
    public String toString() {
      return "Greenhouse water is off";
    }
  }
    public class FansOn extends Event {
        public FansOn(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            fans = true;
        }
        public String toString() {
            return "Greenhouse fans are on";
        }
    }
    public class FansOff extends Event {
        public FansOff(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            fans = false;
        }
        public String toString() {
            return "Greenhouse fans are off";
        }
    }
  public class ThermostatNight extends Event {
    public ThermostatNight(long delayTime) {
      super(delayTime);
    }
    public void action() {
      // Put hardware control code here.
      thermostat = "Night";
    }
    public String toString() {
      return "Thermostat on night setting";
    }
  }
  public class ThermostatDay extends Event {
    public ThermostatDay(long delayTime) {
      super(delayTime);
    }
    public void action() {
      // Put hardware control code here.
      thermostat = "Day";
    }
    public String toString() {
      return "Thermostat on day setting";
    }
  }
  // An example of an action() that inserts a
  // new one of itself into the event list:
  public class Bell extends Event {
      int rings;
      public Bell(long delayTime) {
          super(delayTime);
          rings = 0;
      }
    public Bell(long delayTime, int rings) {
        super(delayTime);
        this.rings = rings;
    }
    public void action() {
	// ring bell rings times if rings is specified
        if(rings > 0) {
            for (int i = 1; i < rings; i++) {
                addEvent(new Bell(2000L * i, 0));
            }
        }
    }
    public String toString() { return "Bing!"; }
  }
    public class WindowMalfunction extends Event {
        public WindowMalfunction(long delayTime) { super(delayTime); }
        public void action() throws ControllerException {
            windowok = false;
            issueOccurred(1,"Greenhouse window is malfunctioning\nInitiating emergency shutdown");
        }
        public String toString() {
            return "Greenhouse window is malfunctioning";
        }
    }
    public class PowerOut extends Event {
        public PowerOut(long delayTime) { super(delayTime); }
        public void action() throws ControllerException {
            poweron = false;
            issueOccurred(2, "Greenhouse power went out\nInitiating emergency shutdown");
        }
        public String toString() {
            return "Greenhouse power is out";
        }
    }
  public class Restart extends Event {
    public Restart(long delayTime, String filename) {
      super(delayTime);
      eventsFile = filename;
    }

    public void action() {
        try {
            File file = new File(eventsFile);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                if(!data.contains("Bell")) {
                    String eventToCall = data.substring(data.lastIndexOf("tme3.Event=") + 6, data.indexOf(","));
                    int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));

                    switch(eventToCall) {
                        case "FansOff":
                            addEvent(new FansOff(delayToUse));
                            break;
                        case "FansOn":
                            addEvent(new FansOn(delayToUse));
                            break;
                        case "LightOff":
                            addEvent(new LightOff(delayToUse));
                            break;
                        case "LightOn":
                            addEvent(new LightOn(delayToUse));
                            break;
                        case "Terminate":
                            addEvent(new Terminate(delayToUse));
                            break;
                        case "ThermostatDay":
                            addEvent(new ThermostatDay(delayToUse));
                            break;
                        case "ThermostatNight":
                            addEvent(new ThermostatNight(delayToUse));
                            break;
                        case "WaterOff":
                            addEvent(new WaterOff(delayToUse));
                            break;
                        case "WaterOn":
                            addEvent(new WaterOn(delayToUse));
                            break;
                        case "WindowMalfunction":
                            addEvent(new WindowMalfunction(delayToUse));
                            break;
                        case "PowerOut":
                            addEvent(new PowerOut(delayToUse));
                            break;
                        default:
                            System.out.println("Something went wrong.");
                            break;
                    }

                }
                else {
                    //String eventToCall = data.substring(data.lastIndexOf("tme3.Event=") + 6, data.indexOf(",time=")); <--keeping for possible future use
                    if(data.contains("rings=")) {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5, data.indexOf(",rings=")));
                        int ringsToDo = Integer.parseInt(data.substring(data.lastIndexOf("rings=") + 6));

                        addEvent(new Bell(delayToUse, ringsToDo));
                    }
                    else {
                        int delayToUse = Integer.parseInt(data.substring(data.lastIndexOf("time=") + 5));

                        addEvent(new Bell(delayToUse));
                    }
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Unable to find \"" + eventsFile + "\".\nCheck capitalization, spelling, and ensure file name ends with \".txt\"");
            addEvent(new Terminate(0));
        }
    }

    public String toString() {
      return "Restarting system";
    }
  }

  void issueOccurred(int errorcode, String errormessage) throws ControllerException {
      this.errorcode = errorcode;
      throw new ControllerException(errormessage);
  }

  public class Terminate extends Event {
    public Terminate(long delayTime) { super(delayTime); }
    public void action() { System.exit(0); }
    public String toString() { return "Terminating";  }
  }

    public class ControllerException extends Exception {
        public ControllerException(String errorMsg) {
            super(errorMsg);
        }

        public String getMessage() {
            return super.getMessage();
        }

        public void shutdown() {

        }
    }

    @Override
    public void shutdown(String message) {
        addEvent(new Terminate(errorcode));
    }

    public static void printUsage() {
    System.out.println("Correct format: ");
    System.out.println("  java GreenhouseControls -f <filename>, or");
    System.out.println("  java GreenhouseControls -d dump.out");
  }

//---------------------------------------------------------
    public static void main(String[] args) {
	try {
	    String option = args[0];
	    String filename = args[1];

	    if ( !(option.equals("-f")) && !(option.equals("-d")) ) {
		System.out.println("Invalid option");
		printUsage();
	    }

	    GreenhouseControls gc = new GreenhouseControls();

	    if (option.equals("-f"))  {
		gc.addEvent(gc.new Restart(0,filename));
	    }

	    gc.run();
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Invalid number of parameters");
	    printUsage();
	}

    catch (ControllerException e) {
        System.out.println(e);
    }
  }

} ///:~
