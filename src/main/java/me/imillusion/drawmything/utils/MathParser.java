package me.imillusion.drawmything.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathParser {

    private static ScriptEngine engine;

    private MathParser()
    {
        //Avoid initializing in utility class
    }

    static {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public static int parseMath(String input)
    {
        try {
            return (int) engine.eval(input);
        } catch (ScriptException e) {
            System.out.println("The formula is invalid");
            e.printStackTrace();
        }

        return 0;
    }
}
