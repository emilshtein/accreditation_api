package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.Random;

public class Util {
    public static ObjectNode createResponse(Object response, boolean ok, boolean accredited) {
        ObjectNode result = Json.newObject();
        result.put("success", ok);
        result.put("accredited", accredited);
        return result;
    }

    //70% success rate for accreditation
    public static boolean generateRandomAnswer() {
        Random rand = new Random();
        if (rand.nextInt(100) > 30)
            return true;
        return false;
    }
}

