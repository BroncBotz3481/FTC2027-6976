package com.danpeled.msftc.dashboard;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TunableNumber {
    private double m_value;
    private String m_name;
    private String m_where;
    private boolean m_mutable = true;

    private static List<TunableNumber> s_tunables = new ArrayList<>();

    private static final Map<Class<?>, String> s_opModeNames = new HashMap<>();

    public static void initTunables() {
        for (TunableNumber tune : s_tunables) {
            tune.initConfig();
        }
    }

    public TunableNumber(String where, String name, double defaultValue) {
        m_where = where;
        m_value = defaultValue;
        m_name = name;
        s_tunables.add(this);
    }

    public TunableNumber(OpMode opMode, String name, double defaultValue) {
        this(getOpModeName(opMode), name, defaultValue);
    }

    public void setMutable(boolean mutable) {
        m_mutable = mutable;
    }

    public TunableNumber immutable() {
        setMutable(true);
        return this;
    }

    public void initConfig() {
        DashboardUtils.uploadConfig(() -> m_value, (v) -> {
            if (m_mutable) m_value = v;
        }, m_where, m_name);
    }

    public double getValueAsDouble() {
        return m_value;
    }

    public void setValue(double value) {
        m_value = value;
    }


    private static String getOpModeName(OpMode opMode) {
        return s_opModeNames.computeIfAbsent(opMode.getClass(), clazz -> {
            TeleOp teleOp = clazz.getAnnotation(TeleOp.class);
            if (teleOp != null) {
                return teleOp.name();
            }

            Autonomous autonomous = clazz.getAnnotation(Autonomous.class);
            if (autonomous != null) {
                return autonomous.name();
            }

            return clazz.getSimpleName();
        });
    }
}
