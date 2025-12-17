package vip.gpg123.dashboard.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Comparison implements Serializable {

    private int pageviews;

    private int visitors;

    private int visits;

    private int bounces;

    private int totaltime;
}
