# AndroidWebServer

Simple experimentation running an http server within an android device (using [nanohttpd](https://github.com/NanoHttpd/nanohttpd)).

Deploying this this app to a device allows you to call `adb shell` commands in the device from the network, using the apps's perspective.
Please note that calling `adb shell` directly from your computer through an USB connected device or emulator have different permissions
and does not behave the same way.

Example:
```shell
$ curl 10.0.1.20:8080?cmd=whoami
[u0_a107]

$ curl "10.0.1.20:8080?cmd=ls%20/proc/uid_stat/1000"
[tcp_rcv, tcp_snd]
```


