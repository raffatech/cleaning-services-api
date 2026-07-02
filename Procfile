web: java -Xmx256m -Xms128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar target/*.jar
# Xmx256m → máximo 256 MB de RAM (Railway free = 512 MB total, isso usa metade)
# Xms128m → começa com 128 MB (cresce conforme necessário)
# XX:+UseG1GC → garbage collector mais rápido (não congela a app)
# XX:MaxGCPauseMillis=200 → pausas de limpeza <200ms (mais responsivo