sudo docker build -t weatherservice -f Dockerfile .
sudo docker run -it --rm -p 5000:8080 weatherservice
