package danta.hippo.templating;

// POC:
// The purpose here is to provide Danta templating support as an OSGi service to an external application

public class Command  {

    public String getName() {
        return Command.class.getName();
    }

    public String getDescription() {
        return "Command Description";
    }

    public boolean execute(String commandline){

        System.out.println("EXECUTED: " + commandline);

        return true;
    }
}
