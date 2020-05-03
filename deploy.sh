set -e

docker build -t zenvia/atm .

docker run -p 8080:8080 -d zenvia/atm
