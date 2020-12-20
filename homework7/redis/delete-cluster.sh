kill $(ps | grep 'redis-server') &

rm nodes-*.conf &
rm *.rdb &
