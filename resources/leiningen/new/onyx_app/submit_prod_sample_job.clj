(ns {{app-name}}.launcher.submit-prod-sample-job
  (:require [clojure.java.io :refer [resource]]
            [{{app-name}}.workflows.sample-workflow :refer [workflow]]
            [{{app-name}}.catalogs.sample-catalog :refer [build-catalog]]
            [{{app-name}}.lifecycles.sample-lifecycle :as sample-lifecycle]
            [{{app-name}}.functions.sample-functions]
            [onyx.plugin.core-async :refer [take-segments!]]
            [onyx.api]))

(defn -main [onyx-id & args]
  (let [cfg (-> "prod-peer-config.edn" resource slurp read-string)
        peer-config (assoc cfg :onyx/id onyx-id)
        lifecycles (sample-lifecycle/build-lifecycles)]
    (let [job {:workflow workflow
               :catalog (build-catalog 20 50)
               :lifecycles lifecycles
               :task-scheduler :onyx.task-scheduler/balanced}]
      (onyx.api/submit-job peer-config job)
      (shutdown-agents))))
