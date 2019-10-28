package com.github.gabryelrock.emailanalyzer.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfluxdbConfig {

    @Value("${influx.url}")
    private String influxUrl;

    @Value("${influx.user}")
    private String influxUser;

    @Value("${influx.password}")
    private String influxPwd;

    @Value("${influx.database}")
    private String influxDatabase;

    public InfluxDB configure() {

        InfluxDB influxDB = InfluxDBFactory.connect(influxUrl, influxUser, influxPwd);

        influxDB.setDatabase(influxDatabase);

        return influxDB;
    }
}
