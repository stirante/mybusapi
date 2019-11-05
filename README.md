# myBusApi

This is an unofficial API from application myBus, which shows bus timetable and position of all buses.

## Simple usage

```java
public class MyBusTest {
    public static void main(String[] args) {
        // Create an api client for city Lublin
        MyBusAPI api = new MyBusAPI(City.LUBLIN);
        // Find first bus stop, that has 'DWORZEC GŁ. PKS' in it's name
        BusStop busStop = api.getDatabase()
                .getStops()
                .stream()
                .filter(stop -> stop.name.contains("DWORZEC GŁ. PKS"))
                .findFirst().orElse(null);
        // If bus stop doesn't exist, then exit
        if (busStop == null) {
            System.out.println("BUS STOP IS NULL");
            return;
        }
        // Get departures from this bus stop
        ArrayList<Departure> timetable = api.getTimetable(busStop.id);
        for (Departure departure : timetable) {
            System.out.println(departure);
        }
        // Get first vehicle from the bus stop and check it's position
        // Fun fact: you can pass both parameters as empty string to get all vehicles in the city with one request
        List<Vehicle> vehicles = api.getVehicles(timetable.get(0).getName(), timetable.get(0).getDirectionSymbol());
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }
}
```

### Prerequisites

Requires at least Java 8 or newer.

## Disclaimer

This is quick and simple project accessing an API, you probably shouldn't access, when creating commercial project.

If you want to commercially use this API, you need to ask for permission the creators of myBus API.
