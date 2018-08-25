(defproject trabajo_saluble "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [tw.edu.ntu.csie/libsvm "3.17"]]
  :main ^:skip-aot trabajo-saluble.core
  :target-path "target/%s"
  :plugins [[lein-git-deps "0.0.1-SNAPSHOT"]]
  :git-dependencies [
  					["https://github.com/tobyhede/monger.git"]
  					["https://github.com/r0man/svm-clj.git"
                       "1d0f89ba11eb699e5b4aecf2eade0bfd44f3f523"]]
  :profiles {:uberjar {:aot :all}})
