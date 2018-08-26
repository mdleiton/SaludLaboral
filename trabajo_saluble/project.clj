(defproject trabajo_saluble "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [tw.edu.ntu.csie/libsvm "3.17"]
                 [org.clojure/data.csv "0.1.3"]
                 [net.mikera/core.matrix "0.45.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [clatrix "0.5.0"]
                 [incanter "1.5.7"]
                [http-kit "2.2.0"]
                ]
  :main ^:skip-aot trabajo-saluble.core
  :target-path "target/%s"
  :plugins [[lein-git-deps "0.0.1-SNAPSHOT"]]
  :profiles {:uberjar {:aot :all}})
