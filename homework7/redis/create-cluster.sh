redis-cli -p 6379 flushdb &
redis-cli -p 6380 flushdb &
redis-cli -p 6381 flushdb &
redis-cli -p 6382 flushdb &
redis-cli -p 6383 flushdb &
redis-cli -p 6384 flushdb &

redis-server a_master.conf &
redis-server b_master.conf &
redis-server c_master.conf &

redis-server a_slave.conf &
redis-server b_slave.conf &
redis-server c_slave.conf &

redis-server sentinel.conf --sentinel > /dev/null/ 2>&1 &

redis-cli --cluster create 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 --cluster-replicas 1
