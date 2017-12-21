package com.algaworks.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Bruno on 08/08/2017.
 */

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

    private String originPermitida = "http://localhost:8000";

    private final Seguranca seguranca = new Seguranca();

    public String getOriginPermitida() {
        return originPermitida;
    }

    public void setOriginPermitida(String originPermitida) {
        this.originPermitida = originPermitida;
    }

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public static class Seguranca {

        private boolean enableHttps;

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }
    }

}
