package client;

import parameters.Worker;

import java.io.Serializable;

public class Request implements Serializable {
    private final String requestName;
    private final String[] parameters;
    private final Worker worker;

    private Request(String requestName, String[] parameters, Worker worker) {
        this.requestName = requestName;
        this.parameters = parameters;
        this.worker = worker;
    }

    public String getRequestName() {
        return requestName;
    }

    public String[] getParameters() {
        return parameters;
    }

    public Worker getWorker() {
        return worker;
    }

    public static class Builder {
        private final String requestName;
        private String[] parameters = null;
        private Worker worker = null;

        public Builder(String requestName) {
            this.requestName = requestName;
        }

        public Builder addParameters(String[] parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addWorker(Worker worker) {
            this.worker = worker;
            return this;
        }

        public Request build() {
            return new Request(requestName, parameters, worker);
        }
    }
}
