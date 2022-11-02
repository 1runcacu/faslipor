<template>
    <div v-for="i in 200" class="snow"></div>
</template>
<style>
.snow {
  position: absolute;
  width: 10px;
  height: 10px;
  background: white;
  border-radius: 50%;
}
.snow:nth-child(1) {
  opacity: 0.2263;
  transform: translate(80.4055vw, -10px) scale(0.8);
  animation: fall-1 26s -2s linear infinite;
}
@keyframes fall-1 {
  54.578% {
    transform: translate(78.2285vw, 54.578vh) scale(0.8);
  }
  to {
    transform: translate(79.317vw, 100vh) scale(0.8);
  }
}
.snow:nth-child(2) {
  opacity: 0.3971;
  transform: translate(90.6858vw, -10px) scale(0.3801);
  animation: fall-2 30s -28s linear infinite;
}
@keyframes fall-2 {
  57.776% {
    transform: translate(91.3253vw, 57.776vh) scale(0.3801);
  }
  to {
    transform: translate(91.00555vw, 100vh) scale(0.3801);
  }
}
.snow:nth-child(3) {
  opacity: 0.3405;
  transform: translate(12.134vw, -10px) scale(0.3399);
  animation: fall-3 15s -22s linear infinite;
}
@keyframes fall-3 {
  50.911% {
    transform: translate(4.6445vw, 50.911vh) scale(0.3399);
  }
  to {
    transform: translate(8.38925vw, 100vh) scale(0.3399);
  }
}
.snow:nth-child(4) {
  opacity: 0.4222;
  transform: translate(61.0848vw, -10px) scale(0.1177);
  animation: fall-4 19s -4s linear infinite;
}
@keyframes fall-4 {
  73.077% {
    transform: translate(63.4296vw, 73.077vh) scale(0.1177);
  }
  to {
    transform: translate(62.2572vw, 100vh) scale(0.1177);
  }
}
.snow:nth-child(5) {
  opacity: 0.5981;
  transform: translate(32.4805vw, -10px) scale(0.1264);
  animation: fall-5 27s -15s linear infinite;
}
@keyframes fall-5 {
  31.277% {
    transform: translate(37.4314vw, 31.277vh) scale(0.1264);
  }
  to {
    transform: translate(34.95595vw, 100vh) scale(0.1264);
  }
}
.snow:nth-child(6) {
  opacity: 0.7888;
  transform: translate(47.9249vw, -10px) scale(0.3312);
  animation: fall-6 14s -9s linear infinite;
}
@keyframes fall-6 {
  75.821% {
    transform: translate(43.6836vw, 75.821vh) scale(0.3312);
  }
  to {
    transform: translate(45.80425vw, 100vh) scale(0.3312);
  }
}
.snow:nth-child(7) {
  opacity: 0.8014;
  transform: translate(7.1831vw, -10px) scale(0.128);
  animation: fall-7 17s -6s linear infinite;
}
@keyframes fall-7 {
  76.114% {
    transform: translate(16.0708vw, 76.114vh) scale(0.128);
  }
  to {
    transform: translate(11.62695vw, 100vh) scale(0.128);
  }
}
.snow:nth-child(8) {
  opacity: 0.0248;
  transform: translate(25.785vw, -10px) scale(0.8015);
  animation: fall-8 11s -3s linear infinite;
}
@keyframes fall-8 {
  79.704% {
    transform: translate(21.9769vw, 79.704vh) scale(0.8015);
  }
  to {
    transform: translate(23.88095vw, 100vh) scale(0.8015);
  }
}
.snow:nth-child(9) {
  opacity: 0.2012;
  transform: translate(40.0258vw, -10px) scale(0.7607);
  animation: fall-9 18s -1s linear infinite;
}
@keyframes fall-9 {
  50.857% {
    transform: translate(43.3499vw, 50.857vh) scale(0.7607);
  }
  to {
    transform: translate(41.68785vw, 100vh) scale(0.7607);
  }
}
.snow:nth-child(10) {
  opacity: 0.2902;
  transform: translate(32.0586vw, -10px) scale(0.2831);
  animation: fall-10 26s -25s linear infinite;
}
@keyframes fall-10 {
  75.748% {
    transform: translate(23.7116vw, 75.748vh) scale(0.2831);
  }
  to {
    transform: translate(27.8851vw, 100vh) scale(0.2831);
  }
}
.snow:nth-child(11) {
  opacity: 0.9439;
  transform: translate(47.684vw, -10px) scale(0.5049);
  animation: fall-11 30s -17s linear infinite;
}
@keyframes fall-11 {
  56.055% {
    transform: translate(57.4867vw, 56.055vh) scale(0.5049);
  }
  to {
    transform: translate(52.58535vw, 100vh) scale(0.5049);
  }
}
.snow:nth-child(12) {
  opacity: 0.14;
  transform: translate(95.5824vw, -10px) scale(0.3891);
  animation: fall-12 22s -6s linear infinite;
}
@keyframes fall-12 {
  36.299% {
    transform: translate(89.1536vw, 36.299vh) scale(0.3891);
  }
  to {
    transform: translate(92.368vw, 100vh) scale(0.3891);
  }
}
.snow:nth-child(13) {
  opacity: 0.2922;
  transform: translate(91.9255vw, -10px) scale(0.3998);
  animation: fall-13 20s -29s linear infinite;
}
@keyframes fall-13 {
  63.813% {
    transform: translate(99.3205vw, 63.813vh) scale(0.3998);
  }
  to {
    transform: translate(95.623vw, 100vh) scale(0.3998);
  }
}
.snow:nth-child(14) {
  opacity: 0.6339;
  transform: translate(39.9723vw, -10px) scale(0.0941);
  animation: fall-14 14s -22s linear infinite;
}
@keyframes fall-14 {
  37.717% {
    transform: translate(47.4214vw, 37.717vh) scale(0.0941);
  }
  to {
    transform: translate(43.69685vw, 100vh) scale(0.0941);
  }
}
.snow:nth-child(15) {
  opacity: 0.5781;
  transform: translate(77.3598vw, -10px) scale(0.1087);
  animation: fall-15 22s -18s linear infinite;
}
@keyframes fall-15 {
  55.696% {
    transform: translate(79.4872vw, 55.696vh) scale(0.1087);
  }
  to {
    transform: translate(78.4235vw, 100vh) scale(0.1087);
  }
}
.snow:nth-child(16) {
  opacity: 0.7494;
  transform: translate(40.8459vw, -10px) scale(0.3865);
  animation: fall-16 15s -1s linear infinite;
}
@keyframes fall-16 {
  35.35% {
    transform: translate(43.3195vw, 35.35vh) scale(0.3865);
  }
  to {
    transform: translate(42.0827vw, 100vh) scale(0.3865);
  }
}
.snow:nth-child(17) {
  opacity: 0.1918;
  transform: translate(22.3358vw, -10px) scale(0.807);
  animation: fall-17 17s -11s linear infinite;
}
@keyframes fall-17 {
  65.601% {
    transform: translate(21.5035vw, 65.601vh) scale(0.807);
  }
  to {
    transform: translate(21.91965vw, 100vh) scale(0.807);
  }
}
.snow:nth-child(18) {
  opacity: 0.5567;
  transform: translate(35.7864vw, -10px) scale(0.5818);
  animation: fall-18 26s -28s linear infinite;
}
@keyframes fall-18 {
  46.681% {
    transform: translate(43.4725vw, 46.681vh) scale(0.5818);
  }
  to {
    transform: translate(39.62945vw, 100vh) scale(0.5818);
  }
}
.snow:nth-child(19) {
  opacity: 0.2493;
  transform: translate(54.8178vw, -10px) scale(0.7314);
  animation: fall-19 24s -14s linear infinite;
}
@keyframes fall-19 {
  69.556% {
    transform: translate(49.0913vw, 69.556vh) scale(0.7314);
  }
  to {
    transform: translate(51.95455vw, 100vh) scale(0.7314);
  }
}
.snow:nth-child(20) {
  opacity: 0.1506;
  transform: translate(60.5313vw, -10px) scale(0.1307);
  animation: fall-20 13s -4s linear infinite;
}
@keyframes fall-20 {
  64.753% {
    transform: translate(58.5577vw, 64.753vh) scale(0.1307);
  }
  to {
    transform: translate(59.5445vw, 100vh) scale(0.1307);
  }
}
.snow:nth-child(21) {
  opacity: 0.1204;
  transform: translate(73.3951vw, -10px) scale(0.5717);
  animation: fall-21 10s -8s linear infinite;
}
@keyframes fall-21 {
  41.243% {
    transform: translate(82.6722vw, 41.243vh) scale(0.5717);
  }
  to {
    transform: translate(78.03365vw, 100vh) scale(0.5717);
  }
}
.snow:nth-child(22) {
  opacity: 0.91;
  transform: translate(96.6175vw, -10px) scale(0.5253);
  animation: fall-22 29s -28s linear infinite;
}
@keyframes fall-22 {
  38.687% {
    transform: translate(103.934vw, 38.687vh) scale(0.5253);
  }
  to {
    transform: translate(100.27575vw, 100vh) scale(0.5253);
  }
}
.snow:nth-child(23) {
  opacity: 0.7236;
  transform: translate(58.5665vw, -10px) scale(0.6742);
  animation: fall-23 27s -13s linear infinite;
}
@keyframes fall-23 {
  49.444% {
    transform: translate(62.026vw, 49.444vh) scale(0.6742);
  }
  to {
    transform: translate(60.29625vw, 100vh) scale(0.6742);
  }
}
.snow:nth-child(24) {
  opacity: 0.4912;
  transform: translate(38.8166vw, -10px) scale(0.8361);
  animation: fall-24 14s -28s linear infinite;
}
@keyframes fall-24 {
  44.531% {
    transform: translate(38.0544vw, 44.531vh) scale(0.8361);
  }
  to {
    transform: translate(38.4355vw, 100vh) scale(0.8361);
  }
}
.snow:nth-child(25) {
  opacity: 0.1127;
  transform: translate(43.7246vw, -10px) scale(0.8988);
  animation: fall-25 17s -12s linear infinite;
}
@keyframes fall-25 {
  55.3% {
    transform: translate(36.815vw, 55.3vh) scale(0.8988);
  }
  to {
    transform: translate(40.2698vw, 100vh) scale(0.8988);
  }
}
.snow:nth-child(26) {
  opacity: 0.8101;
  transform: translate(77.7661vw, -10px) scale(0.4593);
  animation: fall-26 14s -3s linear infinite;
}
@keyframes fall-26 {
  66.132% {
    transform: translate(75.1686vw, 66.132vh) scale(0.4593);
  }
  to {
    transform: translate(76.46735vw, 100vh) scale(0.4593);
  }
}
.snow:nth-child(27) {
  opacity: 0.3996;
  transform: translate(20.0264vw, -10px) scale(0.8503);
  animation: fall-27 22s -4s linear infinite;
}
@keyframes fall-27 {
  79.644% {
    transform: translate(21.8147vw, 79.644vh) scale(0.8503);
  }
  to {
    transform: translate(20.92055vw, 100vh) scale(0.8503);
  }
}
.snow:nth-child(28) {
  opacity: 0.923;
  transform: translate(85.1076vw, -10px) scale(0.3111);
  animation: fall-28 10s -7s linear infinite;
}
@keyframes fall-28 {
  52.386% {
    transform: translate(82.0413vw, 52.386vh) scale(0.3111);
  }
  to {
    transform: translate(83.57445vw, 100vh) scale(0.3111);
  }
}
.snow:nth-child(29) {
  opacity: 0.6571;
  transform: translate(3.5149vw, -10px) scale(0.9323);
  animation: fall-29 26s -13s linear infinite;
}
@keyframes fall-29 {
  32.579% {
    transform: translate(10.3301vw, 32.579vh) scale(0.9323);
  }
  to {
    transform: translate(6.9225vw, 100vh) scale(0.9323);
  }
}
.snow:nth-child(30) {
  opacity: 0.1358;
  transform: translate(73.6857vw, -10px) scale(0.3797);
  animation: fall-30 29s -19s linear infinite;
}
@keyframes fall-30 {
  71.626% {
    transform: translate(71.8462vw, 71.626vh) scale(0.3797);
  }
  to {
    transform: translate(72.76595vw, 100vh) scale(0.3797);
  }
}
.snow:nth-child(31) {
  opacity: 0.8975;
  transform: translate(4.4685vw, -10px) scale(0.0059);
  animation: fall-31 11s -26s linear infinite;
}
@keyframes fall-31 {
  66.886% {
    transform: translate(3.4593vw, 66.886vh) scale(0.0059);
  }
  to {
    transform: translate(3.9639vw, 100vh) scale(0.0059);
  }
}
.snow:nth-child(32) {
  opacity: 0.3747;
  transform: translate(39.0966vw, -10px) scale(0.7017);
  animation: fall-32 12s -21s linear infinite;
}
@keyframes fall-32 {
  48.636% {
    transform: translate(41.6568vw, 48.636vh) scale(0.7017);
  }
  to {
    transform: translate(40.3767vw, 100vh) scale(0.7017);
  }
}
.snow:nth-child(33) {
  opacity: 0.8073;
  transform: translate(61.1467vw, -10px) scale(0.8946);
  animation: fall-33 30s -30s linear infinite;
}
@keyframes fall-33 {
  31.264% {
    transform: translate(59.1341vw, 31.264vh) scale(0.8946);
  }
  to {
    transform: translate(60.1404vw, 100vh) scale(0.8946);
  }
}
.snow:nth-child(34) {
  opacity: 0.0397;
  transform: translate(0.7084vw, -10px) scale(0.026);
  animation: fall-34 16s -11s linear infinite;
}
@keyframes fall-34 {
  61.875% {
    transform: translate(-2.6056vw, 61.875vh) scale(0.026);
  }
  to {
    transform: translate(-0.9486vw, 100vh) scale(0.026);
  }
}
.snow:nth-child(35) {
  opacity: 0.3952;
  transform: translate(94.7684vw, -10px) scale(0.6579);
  animation: fall-35 29s -25s linear infinite;
}
@keyframes fall-35 {
  39.685% {
    transform: translate(101.8126vw, 39.685vh) scale(0.6579);
  }
  to {
    transform: translate(98.2905vw, 100vh) scale(0.6579);
  }
}
.snow:nth-child(36) {
  opacity: 0.0814;
  transform: translate(51.5213vw, -10px) scale(0.4828);
  animation: fall-36 15s -22s linear infinite;
}
@keyframes fall-36 {
  51.447% {
    transform: translate(42.3401vw, 51.447vh) scale(0.4828);
  }
  to {
    transform: translate(46.9307vw, 100vh) scale(0.4828);
  }
}
.snow:nth-child(37) {
  opacity: 0.3357;
  transform: translate(92.0897vw, -10px) scale(0.5827);
  animation: fall-37 24s -10s linear infinite;
}
@keyframes fall-37 {
  75.191% {
    transform: translate(87.7584vw, 75.191vh) scale(0.5827);
  }
  to {
    transform: translate(89.92405vw, 100vh) scale(0.5827);
  }
}
.snow:nth-child(38) {
  opacity: 0.7669;
  transform: translate(29.9021vw, -10px) scale(0.6871);
  animation: fall-38 26s -22s linear infinite;
}
@keyframes fall-38 {
  44.55% {
    transform: translate(26.262vw, 44.55vh) scale(0.6871);
  }
  to {
    transform: translate(28.08205vw, 100vh) scale(0.6871);
  }
}
.snow:nth-child(39) {
  opacity: 0.5822;
  transform: translate(79.3523vw, -10px) scale(0.8594);
  animation: fall-39 24s -14s linear infinite;
}
@keyframes fall-39 {
  49.752% {
    transform: translate(75.6413vw, 49.752vh) scale(0.8594);
  }
  to {
    transform: translate(77.4968vw, 100vh) scale(0.8594);
  }
}
.snow:nth-child(40) {
  opacity: 0.5517;
  transform: translate(53.0073vw, -10px) scale(0.306);
  animation: fall-40 30s -22s linear infinite;
}
@keyframes fall-40 {
  60.385% {
    transform: translate(55.1374vw, 60.385vh) scale(0.306);
  }
  to {
    transform: translate(54.07235vw, 100vh) scale(0.306);
  }
}
.snow:nth-child(41) {
  opacity: 0.2167;
  transform: translate(16.061vw, -10px) scale(0.1391);
  animation: fall-41 18s -28s linear infinite;
}
@keyframes fall-41 {
  77.482% {
    transform: translate(26.0395vw, 77.482vh) scale(0.1391);
  }
  to {
    transform: translate(21.05025vw, 100vh) scale(0.1391);
  }
}
.snow:nth-child(42) {
  opacity: 0.9567;
  transform: translate(94.788vw, -10px) scale(0.9989);
  animation: fall-42 19s -12s linear infinite;
}
@keyframes fall-42 {
  32.894% {
    transform: translate(91.0152vw, 32.894vh) scale(0.9989);
  }
  to {
    transform: translate(92.9016vw, 100vh) scale(0.9989);
  }
}
.snow:nth-child(43) {
  opacity: 0.3009;
  transform: translate(99.9409vw, -10px) scale(0.1979);
  animation: fall-43 22s -3s linear infinite;
}
@keyframes fall-43 {
  78.471% {
    transform: translate(107.8881vw, 78.471vh) scale(0.1979);
  }
  to {
    transform: translate(103.9145vw, 100vh) scale(0.1979);
  }
}
.snow:nth-child(44) {
  opacity: 0.2298;
  transform: translate(76.9259vw, -10px) scale(0.4946);
  animation: fall-44 15s -1s linear infinite;
}
@keyframes fall-44 {
  72.832% {
    transform: translate(77.5431vw, 72.832vh) scale(0.4946);
  }
  to {
    transform: translate(77.2345vw, 100vh) scale(0.4946);
  }
}
.snow:nth-child(45) {
  opacity: 0.2423;
  transform: translate(93.009vw, -10px) scale(0.0993);
  animation: fall-45 14s -22s linear infinite;
}
@keyframes fall-45 {
  66.342% {
    transform: translate(102.9291vw, 66.342vh) scale(0.0993);
  }
  to {
    transform: translate(97.96905vw, 100vh) scale(0.0993);
  }
}
.snow:nth-child(46) {
  opacity: 0.9825;
  transform: translate(3.9547vw, -10px) scale(0.9687);
  animation: fall-46 24s -15s linear infinite;
}
@keyframes fall-46 {
  65.833% {
    transform: translate(4.3015vw, 65.833vh) scale(0.9687);
  }
  to {
    transform: translate(4.1281vw, 100vh) scale(0.9687);
  }
}
.snow:nth-child(47) {
  opacity: 0.0766;
  transform: translate(46.8299vw, -10px) scale(0.9403);
  animation: fall-47 16s -25s linear infinite;
}
@keyframes fall-47 {
  34.701% {
    transform: translate(53.8918vw, 34.701vh) scale(0.9403);
  }
  to {
    transform: translate(50.36085vw, 100vh) scale(0.9403);
  }
}
.snow:nth-child(48) {
  opacity: 0.2252;
  transform: translate(21.8522vw, -10px) scale(0.9529);
  animation: fall-48 12s -4s linear infinite;
}
@keyframes fall-48 {
  69.017% {
    transform: translate(22.3649vw, 69.017vh) scale(0.9529);
  }
  to {
    transform: translate(22.10855vw, 100vh) scale(0.9529);
  }
}
.snow:nth-child(49) {
  opacity: 0.6555;
  transform: translate(30.8065vw, -10px) scale(0.1029);
  animation: fall-49 30s -23s linear infinite;
}
@keyframes fall-49 {
  42.841% {
    transform: translate(33.4296vw, 42.841vh) scale(0.1029);
  }
  to {
    transform: translate(32.11805vw, 100vh) scale(0.1029);
  }
}
.snow:nth-child(50) {
  opacity: 0.026;
  transform: translate(64.4717vw, -10px) scale(0.7314);
  animation: fall-50 25s -24s linear infinite;
}
@keyframes fall-50 {
  33.609% {
    transform: translate(72.0678vw, 33.609vh) scale(0.7314);
  }
  to {
    transform: translate(68.26975vw, 100vh) scale(0.7314);
  }
}
.snow:nth-child(51) {
  opacity: 0.8775;
  transform: translate(68.4009vw, -10px) scale(0.1861);
  animation: fall-51 15s -26s linear infinite;
}
@keyframes fall-51 {
  38.094% {
    transform: translate(71.7541vw, 38.094vh) scale(0.1861);
  }
  to {
    transform: translate(70.0775vw, 100vh) scale(0.1861);
  }
}
.snow:nth-child(52) {
  opacity: 0.5145;
  transform: translate(19.2437vw, -10px) scale(0.8899);
  animation: fall-52 25s -25s linear infinite;
}
@keyframes fall-52 {
  79.406% {
    transform: translate(13.4616vw, 79.406vh) scale(0.8899);
  }
  to {
    transform: translate(16.35265vw, 100vh) scale(0.8899);
  }
}
.snow:nth-child(53) {
  opacity: 0.8511;
  transform: translate(7.0674vw, -10px) scale(0.6795);
  animation: fall-53 14s -18s linear infinite;
}
@keyframes fall-53 {
  58.418% {
    transform: translate(16.8884vw, 58.418vh) scale(0.6795);
  }
  to {
    transform: translate(11.9779vw, 100vh) scale(0.6795);
  }
}
.snow:nth-child(54) {
  opacity: 0.8908;
  transform: translate(41.0013vw, -10px) scale(0.1345);
  animation: fall-54 20s -6s linear infinite;
}
@keyframes fall-54 {
  38.595% {
    transform: translate(38.0949vw, 38.595vh) scale(0.1345);
  }
  to {
    transform: translate(39.5481vw, 100vh) scale(0.1345);
  }
}
.snow:nth-child(55) {
  opacity: 0.088;
  transform: translate(68.8455vw, -10px) scale(0.2785);
  animation: fall-55 29s -18s linear infinite;
}
@keyframes fall-55 {
  32.71% {
    transform: translate(71.2194vw, 32.71vh) scale(0.2785);
  }
  to {
    transform: translate(70.03245vw, 100vh) scale(0.2785);
  }
}
.snow:nth-child(56) {
  opacity: 0.0014;
  transform: translate(16.4038vw, -10px) scale(0.6395);
  animation: fall-56 17s -5s linear infinite;
}
@keyframes fall-56 {
  61.495% {
    transform: translate(23.0442vw, 61.495vh) scale(0.6395);
  }
  to {
    transform: translate(19.724vw, 100vh) scale(0.6395);
  }
}
.snow:nth-child(57) {
  opacity: 0.5724;
  transform: translate(59.2119vw, -10px) scale(0.0512);
  animation: fall-57 19s -22s linear infinite;
}
@keyframes fall-57 {
  48.209% {
    transform: translate(56.6023vw, 48.209vh) scale(0.0512);
  }
  to {
    transform: translate(57.9071vw, 100vh) scale(0.0512);
  }
}
.snow:nth-child(58) {
  opacity: 0.8505;
  transform: translate(68.6254vw, -10px) scale(0.7257);
  animation: fall-58 27s -22s linear infinite;
}
@keyframes fall-58 {
  53.959% {
    transform: translate(59.3198vw, 53.959vh) scale(0.7257);
  }
  to {
    transform: translate(63.9726vw, 100vh) scale(0.7257);
  }
}
.snow:nth-child(59) {
  opacity: 0.3263;
  transform: translate(49.5536vw, -10px) scale(0.2496);
  animation: fall-59 30s -10s linear infinite;
}
@keyframes fall-59 {
  59.299% {
    transform: translate(53.5512vw, 59.299vh) scale(0.2496);
  }
  to {
    transform: translate(51.5524vw, 100vh) scale(0.2496);
  }
}
.snow:nth-child(60) {
  opacity: 0.9639;
  transform: translate(57.2726vw, -10px) scale(0.6147);
  animation: fall-60 23s -18s linear infinite;
}
@keyframes fall-60 {
  41.959% {
    transform: translate(57.3284vw, 41.959vh) scale(0.6147);
  }
  to {
    transform: translate(57.3005vw, 100vh) scale(0.6147);
  }
}
.snow:nth-child(61) {
  opacity: 0.3322;
  transform: translate(86.6728vw, -10px) scale(0.758);
  animation: fall-61 26s -26s linear infinite;
}
@keyframes fall-61 {
  45.658% {
    transform: translate(80.4629vw, 45.658vh) scale(0.758);
  }
  to {
    transform: translate(83.56785vw, 100vh) scale(0.758);
  }
}
.snow:nth-child(62) {
  opacity: 0.0112;
  transform: translate(29.5756vw, -10px) scale(0.7811);
  animation: fall-62 28s -9s linear infinite;
}
@keyframes fall-62 {
  53.937% {
    transform: translate(20.6841vw, 53.937vh) scale(0.7811);
  }
  to {
    transform: translate(25.12985vw, 100vh) scale(0.7811);
  }
}
.snow:nth-child(63) {
  opacity: 0.5917;
  transform: translate(91.3206vw, -10px) scale(0.3389);
  animation: fall-63 28s -1s linear infinite;
}
@keyframes fall-63 {
  49.873% {
    transform: translate(88.8542vw, 49.873vh) scale(0.3389);
  }
  to {
    transform: translate(90.0874vw, 100vh) scale(0.3389);
  }
}
.snow:nth-child(64) {
  opacity: 0.1339;
  transform: translate(42.3447vw, -10px) scale(0.2087);
  animation: fall-64 25s -1s linear infinite;
}
@keyframes fall-64 {
  76.88% {
    transform: translate(45.4642vw, 76.88vh) scale(0.2087);
  }
  to {
    transform: translate(43.90445vw, 100vh) scale(0.2087);
  }
}
.snow:nth-child(65) {
  opacity: 0.3437;
  transform: translate(55.1656vw, -10px) scale(0.7091);
  animation: fall-65 30s -7s linear infinite;
}
@keyframes fall-65 {
  70.131% {
    transform: translate(51.9553vw, 70.131vh) scale(0.7091);
  }
  to {
    transform: translate(53.56045vw, 100vh) scale(0.7091);
  }
}
.snow:nth-child(66) {
  opacity: 0.5521;
  transform: translate(59.1871vw, -10px) scale(0.0752);
  animation: fall-66 22s -12s linear infinite;
}
@keyframes fall-66 {
  66.561% {
    transform: translate(66.4993vw, 66.561vh) scale(0.0752);
  }
  to {
    transform: translate(62.8432vw, 100vh) scale(0.0752);
  }
}
.snow:nth-child(67) {
  opacity: 0.358;
  transform: translate(63.9074vw, -10px) scale(0.8847);
  animation: fall-67 17s -30s linear infinite;
}
@keyframes fall-67 {
  62.515% {
    transform: translate(70.6336vw, 62.515vh) scale(0.8847);
  }
  to {
    transform: translate(67.2705vw, 100vh) scale(0.8847);
  }
}
.snow:nth-child(68) {
  opacity: 0.0369;
  transform: translate(80.0485vw, -10px) scale(0.8974);
  animation: fall-68 11s -7s linear infinite;
}
@keyframes fall-68 {
  34.539% {
    transform: translate(86.278vw, 34.539vh) scale(0.8974);
  }
  to {
    transform: translate(83.16325vw, 100vh) scale(0.8974);
  }
}
.snow:nth-child(69) {
  opacity: 0.0846;
  transform: translate(0.9843vw, -10px) scale(0.5573);
  animation: fall-69 29s -11s linear infinite;
}
@keyframes fall-69 {
  47.897% {
    transform: translate(-7.0231vw, 47.897vh) scale(0.5573);
  }
  to {
    transform: translate(-3.0194vw, 100vh) scale(0.5573);
  }
}
.snow:nth-child(70) {
  opacity: 0.4488;
  transform: translate(76.7134vw, -10px) scale(0.7702);
  animation: fall-70 27s -11s linear infinite;
}
@keyframes fall-70 {
  32.107% {
    transform: translate(75.5285vw, 32.107vh) scale(0.7702);
  }
  to {
    transform: translate(76.12095vw, 100vh) scale(0.7702);
  }
}
.snow:nth-child(71) {
  opacity: 0.9471;
  transform: translate(92.1887vw, -10px) scale(0.0583);
  animation: fall-71 23s -20s linear infinite;
}
@keyframes fall-71 {
  72.671% {
    transform: translate(83.8157vw, 72.671vh) scale(0.0583);
  }
  to {
    transform: translate(88.0022vw, 100vh) scale(0.0583);
  }
}
.snow:nth-child(72) {
  opacity: 0.6849;
  transform: translate(82.6933vw, -10px) scale(0.4735);
  animation: fall-72 22s -1s linear infinite;
}
@keyframes fall-72 {
  40.853% {
    transform: translate(78.5103vw, 40.853vh) scale(0.4735);
  }
  to {
    transform: translate(80.6018vw, 100vh) scale(0.4735);
  }
}
.snow:nth-child(73) {
  opacity: 0.5166;
  transform: translate(24.6718vw, -10px) scale(0.403);
  animation: fall-73 15s -28s linear infinite;
}
@keyframes fall-73 {
  57.915% {
    transform: translate(31.0837vw, 57.915vh) scale(0.403);
  }
  to {
    transform: translate(27.87775vw, 100vh) scale(0.403);
  }
}
.snow:nth-child(74) {
  opacity: 0.3323;
  transform: translate(56.4764vw, -10px) scale(0.783);
  animation: fall-74 28s -13s linear infinite;
}
@keyframes fall-74 {
  32.444% {
    transform: translate(52.8979vw, 32.444vh) scale(0.783);
  }
  to {
    transform: translate(54.68715vw, 100vh) scale(0.783);
  }
}
.snow:nth-child(75) {
  opacity: 0.4599;
  transform: translate(93.9305vw, -10px) scale(0.9909);
  animation: fall-75 22s -29s linear infinite;
}
@keyframes fall-75 {
  67.661% {
    transform: translate(89.2716vw, 67.661vh) scale(0.9909);
  }
  to {
    transform: translate(91.60105vw, 100vh) scale(0.9909);
  }
}
.snow:nth-child(76) {
  opacity: 0.7588;
  transform: translate(32.5286vw, -10px) scale(0.0458);
  animation: fall-76 18s -25s linear infinite;
}
@keyframes fall-76 {
  72.096% {
    transform: translate(26.4023vw, 72.096vh) scale(0.0458);
  }
  to {
    transform: translate(29.46545vw, 100vh) scale(0.0458);
  }
}
.snow:nth-child(77) {
  opacity: 0.9913;
  transform: translate(97.8836vw, -10px) scale(0.1583);
  animation: fall-77 14s -8s linear infinite;
}
@keyframes fall-77 {
  36.361% {
    transform: translate(88.7716vw, 36.361vh) scale(0.1583);
  }
  to {
    transform: translate(93.3276vw, 100vh) scale(0.1583);
  }
}
.snow:nth-child(78) {
  opacity: 0.3244;
  transform: translate(74.6997vw, -10px) scale(0.5114);
  animation: fall-78 25s -9s linear infinite;
}
@keyframes fall-78 {
  38.58% {
    transform: translate(74.0601vw, 38.58vh) scale(0.5114);
  }
  to {
    transform: translate(74.3799vw, 100vh) scale(0.5114);
  }
}
.snow:nth-child(79) {
  opacity: 0.2942;
  transform: translate(30.1664vw, -10px) scale(0.2695);
  animation: fall-79 15s -14s linear infinite;
}
@keyframes fall-79 {
  44.066% {
    transform: translate(24.0112vw, 44.066vh) scale(0.2695);
  }
  to {
    transform: translate(27.0888vw, 100vh) scale(0.2695);
  }
}
.snow:nth-child(80) {
  opacity: 0.7321;
  transform: translate(28.0515vw, -10px) scale(0.1187);
  animation: fall-80 28s -21s linear infinite;
}
@keyframes fall-80 {
  78.302% {
    transform: translate(20.9916vw, 78.302vh) scale(0.1187);
  }
  to {
    transform: translate(24.52155vw, 100vh) scale(0.1187);
  }
}
.snow:nth-child(81) {
  opacity: 0.5392;
  transform: translate(88.6795vw, -10px) scale(0.9156);
  animation: fall-81 20s -30s linear infinite;
}
@keyframes fall-81 {
  77.214% {
    transform: translate(94.366vw, 77.214vh) scale(0.9156);
  }
  to {
    transform: translate(91.52275vw, 100vh) scale(0.9156);
  }
}
.snow:nth-child(82) {
  opacity: 0.965;
  transform: translate(1.5272vw, -10px) scale(0.7493);
  animation: fall-82 15s -30s linear infinite;
}
@keyframes fall-82 {
  47.912% {
    transform: translate(2.6135vw, 47.912vh) scale(0.7493);
  }
  to {
    transform: translate(2.07035vw, 100vh) scale(0.7493);
  }
}
.snow:nth-child(83) {
  opacity: 0.4532;
  transform: translate(68.9942vw, -10px) scale(0.1398);
  animation: fall-83 17s -23s linear infinite;
}
@keyframes fall-83 {
  37.671% {
    transform: translate(70.9975vw, 37.671vh) scale(0.1398);
  }
  to {
    transform: translate(69.99585vw, 100vh) scale(0.1398);
  }
}
.snow:nth-child(84) {
  opacity: 0.5594;
  transform: translate(14.7148vw, -10px) scale(0.0622);
  animation: fall-84 18s -24s linear infinite;
}
@keyframes fall-84 {
  43.105% {
    transform: translate(9.2875vw, 43.105vh) scale(0.0622);
  }
  to {
    transform: translate(12.00115vw, 100vh) scale(0.0622);
  }
}
.snow:nth-child(85) {
  opacity: 0.2695;
  transform: translate(12.5134vw, -10px) scale(0.1198);
  animation: fall-85 27s -25s linear infinite;
}
@keyframes fall-85 {
  60.42% {
    transform: translate(6.3021vw, 60.42vh) scale(0.1198);
  }
  to {
    transform: translate(9.40775vw, 100vh) scale(0.1198);
  }
}
.snow:nth-child(86) {
  opacity: 0.4077;
  transform: translate(80.8378vw, -10px) scale(0.6202);
  animation: fall-86 11s -18s linear infinite;
}
@keyframes fall-86 {
  42.259% {
    transform: translate(87.955vw, 42.259vh) scale(0.6202);
  }
  to {
    transform: translate(84.3964vw, 100vh) scale(0.6202);
  }
}
.snow:nth-child(87) {
  opacity: 0.279;
  transform: translate(9.9431vw, -10px) scale(0.2757);
  animation: fall-87 11s -16s linear infinite;
}
@keyframes fall-87 {
  35.391% {
    transform: translate(12.6392vw, 35.391vh) scale(0.2757);
  }
  to {
    transform: translate(11.29115vw, 100vh) scale(0.2757);
  }
}
.snow:nth-child(88) {
  opacity: 0.0059;
  transform: translate(79.8796vw, -10px) scale(0.3747);
  animation: fall-88 11s -21s linear infinite;
}
@keyframes fall-88 {
  46.97% {
    transform: translate(83.8084vw, 46.97vh) scale(0.3747);
  }
  to {
    transform: translate(81.844vw, 100vh) scale(0.3747);
  }
}
.snow:nth-child(89) {
  opacity: 0.4038;
  transform: translate(1.697vw, -10px) scale(0.2297);
  animation: fall-89 16s -25s linear infinite;
}
@keyframes fall-89 {
  73.879% {
    transform: translate(-7.0012vw, 73.879vh) scale(0.2297);
  }
  to {
    transform: translate(-2.6521vw, 100vh) scale(0.2297);
  }
}
.snow:nth-child(90) {
  opacity: 0.0113;
  transform: translate(29.2324vw, -10px) scale(0.9914);
  animation: fall-90 15s -9s linear infinite;
}
@keyframes fall-90 {
  68.354% {
    transform: translate(30.7717vw, 68.354vh) scale(0.9914);
  }
  to {
    transform: translate(30.00205vw, 100vh) scale(0.9914);
  }
}
.snow:nth-child(91) {
  opacity: 0.7283;
  transform: translate(50.1081vw, -10px) scale(0.444);
  animation: fall-91 18s -21s linear infinite;
}
@keyframes fall-91 {
  77.451% {
    transform: translate(57.4974vw, 77.451vh) scale(0.444);
  }
  to {
    transform: translate(53.80275vw, 100vh) scale(0.444);
  }
}
.snow:nth-child(92) {
  opacity: 0.7934;
  transform: translate(47.8315vw, -10px) scale(0.3385);
  animation: fall-92 25s -3s linear infinite;
}
@keyframes fall-92 {
  33.35% {
    transform: translate(48.7739vw, 33.35vh) scale(0.3385);
  }
  to {
    transform: translate(48.3027vw, 100vh) scale(0.3385);
  }
}
.snow:nth-child(93) {
  opacity: 0.8027;
  transform: translate(40.713vw, -10px) scale(0.499);
  animation: fall-93 20s -10s linear infinite;
}
@keyframes fall-93 {
  46.461% {
    transform: translate(49.156vw, 46.461vh) scale(0.499);
  }
  to {
    transform: translate(44.9345vw, 100vh) scale(0.499);
  }
}
.snow:nth-child(94) {
  opacity: 0.7135;
  transform: translate(44.2737vw, -10px) scale(0.7905);
  animation: fall-94 19s -19s linear infinite;
}
@keyframes fall-94 {
  30.87% {
    transform: translate(50.7237vw, 30.87vh) scale(0.7905);
  }
  to {
    transform: translate(47.4987vw, 100vh) scale(0.7905);
  }
}
.snow:nth-child(95) {
  opacity: 0.8751;
  transform: translate(78.2799vw, -10px) scale(0.7717);
  animation: fall-95 20s -22s linear infinite;
}
@keyframes fall-95 {
  60.439% {
    transform: translate(79.6911vw, 60.439vh) scale(0.7717);
  }
  to {
    transform: translate(78.9855vw, 100vh) scale(0.7717);
  }
}
.snow:nth-child(96) {
  opacity: 0.5926;
  transform: translate(53.2992vw, -10px) scale(0.6368);
  animation: fall-96 27s -29s linear infinite;
}
@keyframes fall-96 {
  45.23% {
    transform: translate(44.1261vw, 45.23vh) scale(0.6368);
  }
  to {
    transform: translate(48.71265vw, 100vh) scale(0.6368);
  }
}
.snow:nth-child(97) {
  opacity: 0.0228;
  transform: translate(37.0162vw, -10px) scale(0.3287);
  animation: fall-97 23s -23s linear infinite;
}
@keyframes fall-97 {
  59.249% {
    transform: translate(34.2911vw, 59.249vh) scale(0.3287);
  }
  to {
    transform: translate(35.65365vw, 100vh) scale(0.3287);
  }
}
.snow:nth-child(98) {
  opacity: 0.0685;
  transform: translate(83.1037vw, -10px) scale(0.8246);
  animation: fall-98 22s -26s linear infinite;
}
@keyframes fall-98 {
  58.881% {
    transform: translate(91.2823vw, 58.881vh) scale(0.8246);
  }
  to {
    transform: translate(87.193vw, 100vh) scale(0.8246);
  }
}
.snow:nth-child(99) {
  opacity: 0.2171;
  transform: translate(29.2178vw, -10px) scale(0.8051);
  animation: fall-99 21s -28s linear infinite;
}
@keyframes fall-99 {
  46.824% {
    transform: translate(24.3072vw, 46.824vh) scale(0.8051);
  }
  to {
    transform: translate(26.7625vw, 100vh) scale(0.8051);
  }
}
.snow:nth-child(100) {
  opacity: 0.7293;
  transform: translate(71.0252vw, -10px) scale(0.7458);
  animation: fall-100 13s -21s linear infinite;
}
@keyframes fall-100 {
  59.168% {
    transform: translate(63.3434vw, 59.168vh) scale(0.7458);
  }
  to {
    transform: translate(67.1843vw, 100vh) scale(0.7458);
  }
}
.snow:nth-child(101) {
  opacity: 0.8758;
  transform: translate(83.9428vw, -10px) scale(0.337);
  animation: fall-101 24s -4s linear infinite;
}
@keyframes fall-101 {
  64.258% {
    transform: translate(91.0005vw, 64.258vh) scale(0.337);
  }
  to {
    transform: translate(87.47165vw, 100vh) scale(0.337);
  }
}
.snow:nth-child(102) {
  opacity: 0.4083;
  transform: translate(36.3756vw, -10px) scale(0.2099);
  animation: fall-102 24s -1s linear infinite;
}
@keyframes fall-102 {
  55.137% {
    transform: translate(42.8964vw, 55.137vh) scale(0.2099);
  }
  to {
    transform: translate(39.636vw, 100vh) scale(0.2099);
  }
}
.snow:nth-child(103) {
  opacity: 0.2341;
  transform: translate(60.1059vw, -10px) scale(0.2343);
  animation: fall-103 28s -3s linear infinite;
}
@keyframes fall-103 {
  70.561% {
    transform: translate(65.0004vw, 70.561vh) scale(0.2343);
  }
  to {
    transform: translate(62.55315vw, 100vh) scale(0.2343);
  }
}
.snow:nth-child(104) {
  opacity: 0.3987;
  transform: translate(42.0708vw, -10px) scale(0.4153);
  animation: fall-104 18s -17s linear infinite;
}
@keyframes fall-104 {
  70.962% {
    transform: translate(43.1212vw, 70.962vh) scale(0.4153);
  }
  to {
    transform: translate(42.596vw, 100vh) scale(0.4153);
  }
}
.snow:nth-child(105) {
  opacity: 0.5237;
  transform: translate(23.7822vw, -10px) scale(0.6086);
  animation: fall-105 16s -7s linear infinite;
}
@keyframes fall-105 {
  72.176% {
    transform: translate(18.431vw, 72.176vh) scale(0.6086);
  }
  to {
    transform: translate(21.1066vw, 100vh) scale(0.6086);
  }
}
.snow:nth-child(106) {
  opacity: 0.9223;
  transform: translate(49.0564vw, -10px) scale(0.5182);
  animation: fall-106 26s -2s linear infinite;
}
@keyframes fall-106 {
  50.027% {
    transform: translate(51.4113vw, 50.027vh) scale(0.5182);
  }
  to {
    transform: translate(50.23385vw, 100vh) scale(0.5182);
  }
}
.snow:nth-child(107) {
  opacity: 0.6668;
  transform: translate(58.4193vw, -10px) scale(0.5862);
  animation: fall-107 13s -1s linear infinite;
}
@keyframes fall-107 {
  58.107% {
    transform: translate(63.3818vw, 58.107vh) scale(0.5862);
  }
  to {
    transform: translate(60.90055vw, 100vh) scale(0.5862);
  }
}
.snow:nth-child(108) {
  opacity: 0.777;
  transform: translate(61.3456vw, -10px) scale(0.5986);
  animation: fall-108 16s -10s linear infinite;
}
@keyframes fall-108 {
  47.026% {
    transform: translate(67.9521vw, 47.026vh) scale(0.5986);
  }
  to {
    transform: translate(64.64885vw, 100vh) scale(0.5986);
  }
}
.snow:nth-child(109) {
  opacity: 0.6323;
  transform: translate(32.5023vw, -10px) scale(0.3858);
  animation: fall-109 10s -15s linear infinite;
}
@keyframes fall-109 {
  64.078% {
    transform: translate(33.3083vw, 64.078vh) scale(0.3858);
  }
  to {
    transform: translate(32.9053vw, 100vh) scale(0.3858);
  }
}
.snow:nth-child(110) {
  opacity: 0.5712;
  transform: translate(10.4989vw, -10px) scale(0.8433);
  animation: fall-110 12s -30s linear infinite;
}
@keyframes fall-110 {
  77.004% {
    transform: translate(15.2203vw, 77.004vh) scale(0.8433);
  }
  to {
    transform: translate(12.8596vw, 100vh) scale(0.8433);
  }
}
.snow:nth-child(111) {
  opacity: 0.4464;
  transform: translate(27.5642vw, -10px) scale(0.6297);
  animation: fall-111 22s -4s linear infinite;
}
@keyframes fall-111 {
  58.481% {
    transform: translate(18.3115vw, 58.481vh) scale(0.6297);
  }
  to {
    transform: translate(22.93785vw, 100vh) scale(0.6297);
  }
}
.snow:nth-child(112) {
  opacity: 0.703;
  transform: translate(25.2571vw, -10px) scale(0.813);
  animation: fall-112 18s -13s linear infinite;
}
@keyframes fall-112 {
  35.105% {
    transform: translate(26.7359vw, 35.105vh) scale(0.813);
  }
  to {
    transform: translate(25.9965vw, 100vh) scale(0.813);
  }
}
.snow:nth-child(113) {
  opacity: 0.8561;
  transform: translate(25.6739vw, -10px) scale(0.3004);
  animation: fall-113 10s -18s linear infinite;
}
@keyframes fall-113 {
  49.297% {
    transform: translate(20.3902vw, 49.297vh) scale(0.3004);
  }
  to {
    transform: translate(23.03205vw, 100vh) scale(0.3004);
  }
}
.snow:nth-child(114) {
  opacity: 0.0067;
  transform: translate(58.4064vw, -10px) scale(0.742);
  animation: fall-114 12s -11s linear infinite;
}
@keyframes fall-114 {
  35.647% {
    transform: translate(49.2857vw, 35.647vh) scale(0.742);
  }
  to {
    transform: translate(53.84605vw, 100vh) scale(0.742);
  }
}
.snow:nth-child(115) {
  opacity: 0.9911;
  transform: translate(29.2899vw, -10px) scale(0.3141);
  animation: fall-115 26s -22s linear infinite;
}
@keyframes fall-115 {
  51.601% {
    transform: translate(35.7008vw, 51.601vh) scale(0.3141);
  }
  to {
    transform: translate(32.49535vw, 100vh) scale(0.3141);
  }
}
.snow:nth-child(116) {
  opacity: 0.7299;
  transform: translate(51.408vw, -10px) scale(0.5177);
  animation: fall-116 24s -12s linear infinite;
}
@keyframes fall-116 {
  72.761% {
    transform: translate(57.8871vw, 72.761vh) scale(0.5177);
  }
  to {
    transform: translate(54.64755vw, 100vh) scale(0.5177);
  }
}
.snow:nth-child(117) {
  opacity: 0.1111;
  transform: translate(7.4882vw, -10px) scale(0.0569);
  animation: fall-117 18s -26s linear infinite;
}
@keyframes fall-117 {
  49.866% {
    transform: translate(15.1861vw, 49.866vh) scale(0.0569);
  }
  to {
    transform: translate(11.33715vw, 100vh) scale(0.0569);
  }
}
.snow:nth-child(118) {
  opacity: 0.1938;
  transform: translate(88.6949vw, -10px) scale(0.6359);
  animation: fall-118 14s -20s linear infinite;
}
@keyframes fall-118 {
  52.207% {
    transform: translate(92.0742vw, 52.207vh) scale(0.6359);
  }
  to {
    transform: translate(90.38455vw, 100vh) scale(0.6359);
  }
}
.snow:nth-child(119) {
  opacity: 0.9931;
  transform: translate(35.1406vw, -10px) scale(0.3017);
  animation: fall-119 21s -15s linear infinite;
}
@keyframes fall-119 {
  48.249% {
    transform: translate(43.4453vw, 48.249vh) scale(0.3017);
  }
  to {
    transform: translate(39.29295vw, 100vh) scale(0.3017);
  }
}
.snow:nth-child(120) {
  opacity: 0.8325;
  transform: translate(25.7542vw, -10px) scale(0.3409);
  animation: fall-120 27s -11s linear infinite;
}
@keyframes fall-120 {
  71.562% {
    transform: translate(32.7447vw, 71.562vh) scale(0.3409);
  }
  to {
    transform: translate(29.24945vw, 100vh) scale(0.3409);
  }
}
.snow:nth-child(121) {
  opacity: 0.2063;
  transform: translate(1.8907vw, -10px) scale(0.5523);
  animation: fall-121 25s -6s linear infinite;
}
@keyframes fall-121 {
  45.658% {
    transform: translate(-7.2494vw, 45.658vh) scale(0.5523);
  }
  to {
    transform: translate(-2.67935vw, 100vh) scale(0.5523);
  }
}
.snow:nth-child(122) {
  opacity: 0.6588;
  transform: translate(24.0407vw, -10px) scale(0.4286);
  animation: fall-122 26s -21s linear infinite;
}
@keyframes fall-122 {
  56.593% {
    transform: translate(17.5638vw, 56.593vh) scale(0.4286);
  }
  to {
    transform: translate(20.80225vw, 100vh) scale(0.4286);
  }
}
.snow:nth-child(123) {
  opacity: 0.8892;
  transform: translate(8.4709vw, -10px) scale(0.6999);
  animation: fall-123 16s -11s linear infinite;
}
@keyframes fall-123 {
  66.425% {
    transform: translate(17.9667vw, 66.425vh) scale(0.6999);
  }
  to {
    transform: translate(13.2188vw, 100vh) scale(0.6999);
  }
}
.snow:nth-child(124) {
  opacity: 0.7823;
  transform: translate(52.906vw, -10px) scale(0.3706);
  animation: fall-124 22s -12s linear infinite;
}
@keyframes fall-124 {
  53.766% {
    transform: translate(54.1479vw, 53.766vh) scale(0.3706);
  }
  to {
    transform: translate(53.52695vw, 100vh) scale(0.3706);
  }
}
.snow:nth-child(125) {
  opacity: 0.695;
  transform: translate(52.4879vw, -10px) scale(0.0859);
  animation: fall-125 21s -28s linear infinite;
}
@keyframes fall-125 {
  78.7% {
    transform: translate(54.2012vw, 78.7vh) scale(0.0859);
  }
  to {
    transform: translate(53.34455vw, 100vh) scale(0.0859);
  }
}
.snow:nth-child(126) {
  opacity: 0.6142;
  transform: translate(20.4475vw, -10px) scale(0.6249);
  animation: fall-126 30s -2s linear infinite;
}
@keyframes fall-126 {
  35.169% {
    transform: translate(24.5159vw, 35.169vh) scale(0.6249);
  }
  to {
    transform: translate(22.4817vw, 100vh) scale(0.6249);
  }
}
.snow:nth-child(127) {
  opacity: 0.936;
  transform: translate(59.3051vw, -10px) scale(0.8036);
  animation: fall-127 16s -10s linear infinite;
}
@keyframes fall-127 {
  43.265% {
    transform: translate(64.9616vw, 43.265vh) scale(0.8036);
  }
  to {
    transform: translate(62.13335vw, 100vh) scale(0.8036);
  }
}
.snow:nth-child(128) {
  opacity: 0.0215;
  transform: translate(93.1583vw, -10px) scale(0.7854);
  animation: fall-128 30s -12s linear infinite;
}
@keyframes fall-128 {
  75.175% {
    transform: translate(87.1823vw, 75.175vh) scale(0.7854);
  }
  to {
    transform: translate(90.1703vw, 100vh) scale(0.7854);
  }
}
.snow:nth-child(129) {
  opacity: 0.4042;
  transform: translate(6.0693vw, -10px) scale(0.2522);
  animation: fall-129 22s -8s linear infinite;
}
@keyframes fall-129 {
  47.186% {
    transform: translate(9.7219vw, 47.186vh) scale(0.2522);
  }
  to {
    transform: translate(7.8956vw, 100vh) scale(0.2522);
  }
}
.snow:nth-child(130) {
  opacity: 0.237;
  transform: translate(96.3094vw, -10px) scale(0.4055);
  animation: fall-130 23s -1s linear infinite;
}
@keyframes fall-130 {
  36.61% {
    transform: translate(105.8628vw, 36.61vh) scale(0.4055);
  }
  to {
    transform: translate(101.0861vw, 100vh) scale(0.4055);
  }
}
.snow:nth-child(131) {
  opacity: 0.4765;
  transform: translate(46.4039vw, -10px) scale(0.5645);
  animation: fall-131 11s -18s linear infinite;
}
@keyframes fall-131 {
  66.468% {
    transform: translate(53.1535vw, 66.468vh) scale(0.5645);
  }
  to {
    transform: translate(49.7787vw, 100vh) scale(0.5645);
  }
}
.snow:nth-child(132) {
  opacity: 0.04;
  transform: translate(93.5918vw, -10px) scale(0.0051);
  animation: fall-132 15s -18s linear infinite;
}
@keyframes fall-132 {
  33.793% {
    transform: translate(87.843vw, 33.793vh) scale(0.0051);
  }
  to {
    transform: translate(90.7174vw, 100vh) scale(0.0051);
  }
}
.snow:nth-child(133) {
  opacity: 0.9053;
  transform: translate(17.0896vw, -10px) scale(0.7152);
  animation: fall-133 24s -6s linear infinite;
}
@keyframes fall-133 {
  67.971% {
    transform: translate(13.6885vw, 67.971vh) scale(0.7152);
  }
  to {
    transform: translate(15.38905vw, 100vh) scale(0.7152);
  }
}
.snow:nth-child(134) {
  opacity: 0.4777;
  transform: translate(33.1474vw, -10px) scale(0.9346);
  animation: fall-134 21s -28s linear infinite;
}
@keyframes fall-134 {
  49.248% {
    transform: translate(38.38vw, 49.248vh) scale(0.9346);
  }
  to {
    transform: translate(35.7637vw, 100vh) scale(0.9346);
  }
}
.snow:nth-child(135) {
  opacity: 0.9364;
  transform: translate(72.2323vw, -10px) scale(0.619);
  animation: fall-135 23s -28s linear infinite;
}
@keyframes fall-135 {
  73.823% {
    transform: translate(66.2218vw, 73.823vh) scale(0.619);
  }
  to {
    transform: translate(69.22705vw, 100vh) scale(0.619);
  }
}
.snow:nth-child(136) {
  opacity: 0.41;
  transform: translate(39.311vw, -10px) scale(0.0053);
  animation: fall-136 20s -17s linear infinite;
}
@keyframes fall-136 {
  53.682% {
    transform: translate(30.9854vw, 53.682vh) scale(0.0053);
  }
  to {
    transform: translate(35.1482vw, 100vh) scale(0.0053);
  }
}
.snow:nth-child(137) {
  opacity: 0.8943;
  transform: translate(89.8086vw, -10px) scale(0.3398);
  animation: fall-137 27s -21s linear infinite;
}
@keyframes fall-137 {
  78.736% {
    transform: translate(87.7876vw, 78.736vh) scale(0.3398);
  }
  to {
    transform: translate(88.7981vw, 100vh) scale(0.3398);
  }
}
.snow:nth-child(138) {
  opacity: 0.2632;
  transform: translate(33.9708vw, -10px) scale(0.7717);
  animation: fall-138 27s -6s linear infinite;
}
@keyframes fall-138 {
  64.68% {
    transform: translate(40.353vw, 64.68vh) scale(0.7717);
  }
  to {
    transform: translate(37.1619vw, 100vh) scale(0.7717);
  }
}
.snow:nth-child(139) {
  opacity: 0.9079;
  transform: translate(67.3846vw, -10px) scale(0.6724);
  animation: fall-139 19s -24s linear infinite;
}
@keyframes fall-139 {
  62.27% {
    transform: translate(58.5331vw, 62.27vh) scale(0.6724);
  }
  to {
    transform: translate(62.95885vw, 100vh) scale(0.6724);
  }
}
.snow:nth-child(140) {
  opacity: 0.5076;
  transform: translate(81.3323vw, -10px) scale(0.2256);
  animation: fall-140 16s -8s linear infinite;
}
@keyframes fall-140 {
  35.931% {
    transform: translate(90.3349vw, 35.931vh) scale(0.2256);
  }
  to {
    transform: translate(85.8336vw, 100vh) scale(0.2256);
  }
}
.snow:nth-child(141) {
  opacity: 0.8063;
  transform: translate(54.2788vw, -10px) scale(0.4897);
  animation: fall-141 15s -21s linear infinite;
}
@keyframes fall-141 {
  78.913% {
    transform: translate(58.8819vw, 78.913vh) scale(0.4897);
  }
  to {
    transform: translate(56.58035vw, 100vh) scale(0.4897);
  }
}
.snow:nth-child(142) {
  opacity: 0.0406;
  transform: translate(40.8003vw, -10px) scale(0.4845);
  animation: fall-142 23s -22s linear infinite;
}
@keyframes fall-142 {
  60.007% {
    transform: translate(32.8409vw, 60.007vh) scale(0.4845);
  }
  to {
    transform: translate(36.8206vw, 100vh) scale(0.4845);
  }
}
.snow:nth-child(143) {
  opacity: 0.3959;
  transform: translate(90.3975vw, -10px) scale(0.3048);
  animation: fall-143 20s -15s linear infinite;
}
@keyframes fall-143 {
  52.808% {
    transform: translate(92.8056vw, 52.808vh) scale(0.3048);
  }
  to {
    transform: translate(91.60155vw, 100vh) scale(0.3048);
  }
}
.snow:nth-child(144) {
  opacity: 0.1842;
  transform: translate(73.2587vw, -10px) scale(0.1824);
  animation: fall-144 27s -12s linear infinite;
}
@keyframes fall-144 {
  33.628% {
    transform: translate(77.424vw, 33.628vh) scale(0.1824);
  }
  to {
    transform: translate(75.34135vw, 100vh) scale(0.1824);
  }
}
.snow:nth-child(145) {
  opacity: 0.1467;
  transform: translate(38.5607vw, -10px) scale(0.1991);
  animation: fall-145 18s -14s linear infinite;
}
@keyframes fall-145 {
  65.787% {
    transform: translate(46.134vw, 65.787vh) scale(0.1991);
  }
  to {
    transform: translate(42.34735vw, 100vh) scale(0.1991);
  }
}
.snow:nth-child(146) {
  opacity: 0.0578;
  transform: translate(36.2093vw, -10px) scale(0.7629);
  animation: fall-146 28s -23s linear infinite;
}
@keyframes fall-146 {
  54.007% {
    transform: translate(39.4897vw, 54.007vh) scale(0.7629);
  }
  to {
    transform: translate(37.8495vw, 100vh) scale(0.7629);
  }
}
.snow:nth-child(147) {
  opacity: 0.7672;
  transform: translate(14.6586vw, -10px) scale(0.4878);
  animation: fall-147 27s -21s linear infinite;
}
@keyframes fall-147 {
  76.742% {
    transform: translate(10.915vw, 76.742vh) scale(0.4878);
  }
  to {
    transform: translate(12.7868vw, 100vh) scale(0.4878);
  }
}
.snow:nth-child(148) {
  opacity: 0.3308;
  transform: translate(68.6205vw, -10px) scale(0.8529);
  animation: fall-148 26s -9s linear infinite;
}
@keyframes fall-148 {
  31.089% {
    transform: translate(61.147vw, 31.089vh) scale(0.8529);
  }
  to {
    transform: translate(64.88375vw, 100vh) scale(0.8529);
  }
}
.snow:nth-child(149) {
  opacity: 0.2949;
  transform: translate(68.6671vw, -10px) scale(0.3832);
  animation: fall-149 13s -20s linear infinite;
}
@keyframes fall-149 {
  34.587% {
    transform: translate(78.1838vw, 34.587vh) scale(0.3832);
  }
  to {
    transform: translate(73.42545vw, 100vh) scale(0.3832);
  }
}
.snow:nth-child(150) {
  opacity: 0.6466;
  transform: translate(20.7277vw, -10px) scale(0.505);
  animation: fall-150 30s -24s linear infinite;
}
@keyframes fall-150 {
  67.574% {
    transform: translate(20.7152vw, 67.574vh) scale(0.505);
  }
  to {
    transform: translate(20.72145vw, 100vh) scale(0.505);
  }
}
.snow:nth-child(151) {
  opacity: 0.9269;
  transform: translate(81.4957vw, -10px) scale(0.0875);
  animation: fall-151 12s -28s linear infinite;
}
@keyframes fall-151 {
  57.742% {
    transform: translate(89.6985vw, 57.742vh) scale(0.0875);
  }
  to {
    transform: translate(85.5971vw, 100vh) scale(0.0875);
  }
}
.snow:nth-child(152) {
  opacity: 0.7521;
  transform: translate(8.0666vw, -10px) scale(0.6157);
  animation: fall-152 23s -11s linear infinite;
}
@keyframes fall-152 {
  52.106% {
    transform: translate(3.8837vw, 52.106vh) scale(0.6157);
  }
  to {
    transform: translate(5.97515vw, 100vh) scale(0.6157);
  }
}
.snow:nth-child(153) {
  opacity: 0.9794;
  transform: translate(65.4599vw, -10px) scale(0.1828);
  animation: fall-153 11s -29s linear infinite;
}
@keyframes fall-153 {
  45.974% {
    transform: translate(74.8338vw, 45.974vh) scale(0.1828);
  }
  to {
    transform: translate(70.14685vw, 100vh) scale(0.1828);
  }
}
.snow:nth-child(154) {
  opacity: 0.0205;
  transform: translate(11.3719vw, -10px) scale(0.3504);
  animation: fall-154 28s -27s linear infinite;
}
@keyframes fall-154 {
  35.342% {
    transform: translate(3.588vw, 35.342vh) scale(0.3504);
  }
  to {
    transform: translate(7.47995vw, 100vh) scale(0.3504);
  }
}
.snow:nth-child(155) {
  opacity: 0.4369;
  transform: translate(19.3496vw, -10px) scale(0.4873);
  animation: fall-155 13s -21s linear infinite;
}
@keyframes fall-155 {
  32.254% {
    transform: translate(18.1563vw, 32.254vh) scale(0.4873);
  }
  to {
    transform: translate(18.75295vw, 100vh) scale(0.4873);
  }
}
.snow:nth-child(156) {
  opacity: 0.1637;
  transform: translate(2.1447vw, -10px) scale(0.9626);
  animation: fall-156 18s -2s linear infinite;
}
@keyframes fall-156 {
  55.862% {
    transform: translate(9.4781vw, 55.862vh) scale(0.9626);
  }
  to {
    transform: translate(5.8114vw, 100vh) scale(0.9626);
  }
}
.snow:nth-child(157) {
  opacity: 0.957;
  transform: translate(96.1876vw, -10px) scale(0.6934);
  animation: fall-157 19s -27s linear infinite;
}
@keyframes fall-157 {
  75.939% {
    transform: translate(97.2815vw, 75.939vh) scale(0.6934);
  }
  to {
    transform: translate(96.73455vw, 100vh) scale(0.6934);
  }
}
.snow:nth-child(158) {
  opacity: 0.8904;
  transform: translate(77.4429vw, -10px) scale(0.2708);
  animation: fall-158 27s -18s linear infinite;
}
@keyframes fall-158 {
  55.677% {
    transform: translate(69.2249vw, 55.677vh) scale(0.2708);
  }
  to {
    transform: translate(73.3339vw, 100vh) scale(0.2708);
  }
}
.snow:nth-child(159) {
  opacity: 0.4215;
  transform: translate(54.7289vw, -10px) scale(0.438);
  animation: fall-159 29s -29s linear infinite;
}
@keyframes fall-159 {
  33.329% {
    transform: translate(60.1067vw, 33.329vh) scale(0.438);
  }
  to {
    transform: translate(57.4178vw, 100vh) scale(0.438);
  }
}
.snow:nth-child(160) {
  opacity: 0.455;
  transform: translate(97.3044vw, -10px) scale(0.4253);
  animation: fall-160 11s -9s linear infinite;
}
@keyframes fall-160 {
  59.297% {
    transform: translate(102.3661vw, 59.297vh) scale(0.4253);
  }
  to {
    transform: translate(99.83525vw, 100vh) scale(0.4253);
  }
}
.snow:nth-child(161) {
  opacity: 0.7696;
  transform: translate(85.2651vw, -10px) scale(0.0916);
  animation: fall-161 22s -17s linear infinite;
}
@keyframes fall-161 {
  39.128% {
    transform: translate(83.5779vw, 39.128vh) scale(0.0916);
  }
  to {
    transform: translate(84.4215vw, 100vh) scale(0.0916);
  }
}
.snow:nth-child(162) {
  opacity: 0.9098;
  transform: translate(6.4202vw, -10px) scale(0.6026);
  animation: fall-162 20s -22s linear infinite;
}
@keyframes fall-162 {
  73.124% {
    transform: translate(16.389vw, 73.124vh) scale(0.6026);
  }
  to {
    transform: translate(11.4046vw, 100vh) scale(0.6026);
  }
}
.snow:nth-child(163) {
  opacity: 0.5894;
  transform: translate(87.8151vw, -10px) scale(0.8873);
  animation: fall-163 22s -1s linear infinite;
}
@keyframes fall-163 {
  64.918% {
    transform: translate(95.6412vw, 64.918vh) scale(0.8873);
  }
  to {
    transform: translate(91.72815vw, 100vh) scale(0.8873);
  }
}
.snow:nth-child(164) {
  opacity: 0.8216;
  transform: translate(1.5457vw, -10px) scale(0.1396);
  animation: fall-164 29s -3s linear infinite;
}
@keyframes fall-164 {
  74.041% {
    transform: translate(-2.0695vw, 74.041vh) scale(0.1396);
  }
  to {
    transform: translate(-0.2619vw, 100vh) scale(0.1396);
  }
}
.snow:nth-child(165) {
  opacity: 0.0068;
  transform: translate(29.4239vw, -10px) scale(0.6554);
  animation: fall-165 10s -28s linear infinite;
}
@keyframes fall-165 {
  60.676% {
    transform: translate(23.1817vw, 60.676vh) scale(0.6554);
  }
  to {
    transform: translate(26.3028vw, 100vh) scale(0.6554);
  }
}
.snow:nth-child(166) {
  opacity: 0.0978;
  transform: translate(77.338vw, -10px) scale(0.8528);
  animation: fall-166 16s -25s linear infinite;
}
@keyframes fall-166 {
  38.018% {
    transform: translate(74.9874vw, 38.018vh) scale(0.8528);
  }
  to {
    transform: translate(76.1627vw, 100vh) scale(0.8528);
  }
}
.snow:nth-child(167) {
  opacity: 0.5112;
  transform: translate(43.3012vw, -10px) scale(0.1396);
  animation: fall-167 19s -23s linear infinite;
}
@keyframes fall-167 {
  41.744% {
    transform: translate(46.7311vw, 41.744vh) scale(0.1396);
  }
  to {
    transform: translate(45.01615vw, 100vh) scale(0.1396);
  }
}
.snow:nth-child(168) {
  opacity: 0.054;
  transform: translate(3.1289vw, -10px) scale(0.4462);
  animation: fall-168 28s -15s linear infinite;
}
@keyframes fall-168 {
  68.34% {
    transform: translate(12.3287vw, 68.34vh) scale(0.4462);
  }
  to {
    transform: translate(7.7288vw, 100vh) scale(0.4462);
  }
}
.snow:nth-child(169) {
  opacity: 0.5396;
  transform: translate(73.1747vw, -10px) scale(0.6605);
  animation: fall-169 30s -26s linear infinite;
}
@keyframes fall-169 {
  38.671% {
    transform: translate(76.7646vw, 38.671vh) scale(0.6605);
  }
  to {
    transform: translate(74.96965vw, 100vh) scale(0.6605);
  }
}
.snow:nth-child(170) {
  opacity: 0.1617;
  transform: translate(71.2067vw, -10px) scale(0.2263);
  animation: fall-170 30s -6s linear infinite;
}
@keyframes fall-170 {
  40.019% {
    transform: translate(76.7077vw, 40.019vh) scale(0.2263);
  }
  to {
    transform: translate(73.9572vw, 100vh) scale(0.2263);
  }
}
.snow:nth-child(171) {
  opacity: 0.0847;
  transform: translate(16.5448vw, -10px) scale(0.9435);
  animation: fall-171 28s -14s linear infinite;
}
@keyframes fall-171 {
  45.064% {
    transform: translate(23.2377vw, 45.064vh) scale(0.9435);
  }
  to {
    transform: translate(19.89125vw, 100vh) scale(0.9435);
  }
}
.snow:nth-child(172) {
  opacity: 0.2202;
  transform: translate(39.4806vw, -10px) scale(0.768);
  animation: fall-172 20s -15s linear infinite;
}
@keyframes fall-172 {
  74.085% {
    transform: translate(47.226vw, 74.085vh) scale(0.768);
  }
  to {
    transform: translate(43.3533vw, 100vh) scale(0.768);
  }
}
.snow:nth-child(173) {
  opacity: 0.6482;
  transform: translate(34.5717vw, -10px) scale(0.3245);
  animation: fall-173 18s -9s linear infinite;
}
@keyframes fall-173 {
  46.108% {
    transform: translate(29.9773vw, 46.108vh) scale(0.3245);
  }
  to {
    transform: translate(32.2745vw, 100vh) scale(0.3245);
  }
}
.snow:nth-child(174) {
  opacity: 0.6309;
  transform: translate(77.78vw, -10px) scale(0.7601);
  animation: fall-174 29s -19s linear infinite;
}
@keyframes fall-174 {
  65.231% {
    transform: translate(70.048vw, 65.231vh) scale(0.7601);
  }
  to {
    transform: translate(73.914vw, 100vh) scale(0.7601);
  }
}
.snow:nth-child(175) {
  opacity: 0.1226;
  transform: translate(84.8693vw, -10px) scale(0.7987);
  animation: fall-175 16s -13s linear infinite;
}
@keyframes fall-175 {
  61.885% {
    transform: translate(75.6721vw, 61.885vh) scale(0.7987);
  }
  to {
    transform: translate(80.2707vw, 100vh) scale(0.7987);
  }
}
.snow:nth-child(176) {
  opacity: 0.9794;
  transform: translate(48.6524vw, -10px) scale(0.5028);
  animation: fall-176 14s -8s linear infinite;
}
@keyframes fall-176 {
  58.418% {
    transform: translate(43.2762vw, 58.418vh) scale(0.5028);
  }
  to {
    transform: translate(45.9643vw, 100vh) scale(0.5028);
  }
}
.snow:nth-child(177) {
  opacity: 0.4267;
  transform: translate(10.1574vw, -10px) scale(0.8048);
  animation: fall-177 21s -19s linear infinite;
}
@keyframes fall-177 {
  42.27% {
    transform: translate(2.1949vw, 42.27vh) scale(0.8048);
  }
  to {
    transform: translate(6.17615vw, 100vh) scale(0.8048);
  }
}
.snow:nth-child(178) {
  opacity: 0.5359;
  transform: translate(47.6265vw, -10px) scale(0.3012);
  animation: fall-178 30s -16s linear infinite;
}
@keyframes fall-178 {
  35.801% {
    transform: translate(40.388vw, 35.801vh) scale(0.3012);
  }
  to {
    transform: translate(44.00725vw, 100vh) scale(0.3012);
  }
}
.snow:nth-child(179) {
  opacity: 0.6683;
  transform: translate(94.3791vw, -10px) scale(0.0936);
  animation: fall-179 21s -1s linear infinite;
}
@keyframes fall-179 {
  32.789% {
    transform: translate(101.8679vw, 32.789vh) scale(0.0936);
  }
  to {
    transform: translate(98.1235vw, 100vh) scale(0.0936);
  }
}
.snow:nth-child(180) {
  opacity: 0.4727;
  transform: translate(67.0703vw, -10px) scale(0.427);
  animation: fall-180 22s -14s linear infinite;
}
@keyframes fall-180 {
  47.787% {
    transform: translate(64.7052vw, 47.787vh) scale(0.427);
  }
  to {
    transform: translate(65.88775vw, 100vh) scale(0.427);
  }
}
.snow:nth-child(181) {
  opacity: 0.7926;
  transform: translate(23.0852vw, -10px) scale(0.2711);
  animation: fall-181 25s -6s linear infinite;
}
@keyframes fall-181 {
  34.171% {
    transform: translate(32.9073vw, 34.171vh) scale(0.2711);
  }
  to {
    transform: translate(27.99625vw, 100vh) scale(0.2711);
  }
}
.snow:nth-child(182) {
  opacity: 0.1201;
  transform: translate(23.2774vw, -10px) scale(0.5177);
  animation: fall-182 15s -13s linear infinite;
}
@keyframes fall-182 {
  75.702% {
    transform: translate(21.3149vw, 75.702vh) scale(0.5177);
  }
  to {
    transform: translate(22.29615vw, 100vh) scale(0.5177);
  }
}
.snow:nth-child(183) {
  opacity: 0.3483;
  transform: translate(14.5794vw, -10px) scale(0.2862);
  animation: fall-183 28s -21s linear infinite;
}
@keyframes fall-183 {
  31.697% {
    transform: translate(10.7921vw, 31.697vh) scale(0.2862);
  }
  to {
    transform: translate(12.68575vw, 100vh) scale(0.2862);
  }
}
.snow:nth-child(184) {
  opacity: 0.2266;
  transform: translate(44.3725vw, -10px) scale(0.0181);
  animation: fall-184 25s -20s linear infinite;
}
@keyframes fall-184 {
  52.905% {
    transform: translate(37.3129vw, 52.905vh) scale(0.0181);
  }
  to {
    transform: translate(40.8427vw, 100vh) scale(0.0181);
  }
}
.snow:nth-child(185) {
  opacity: 0.8195;
  transform: translate(91.645vw, -10px) scale(0.5813);
  animation: fall-185 29s -12s linear infinite;
}
@keyframes fall-185 {
  68.716% {
    transform: translate(81.9385vw, 68.716vh) scale(0.5813);
  }
  to {
    transform: translate(86.79175vw, 100vh) scale(0.5813);
  }
}
.snow:nth-child(186) {
  opacity: 0.2814;
  transform: translate(31.2004vw, -10px) scale(0.1831);
  animation: fall-186 21s -2s linear infinite;
}
@keyframes fall-186 {
  60.402% {
    transform: translate(40.5573vw, 60.402vh) scale(0.1831);
  }
  to {
    transform: translate(35.87885vw, 100vh) scale(0.1831);
  }
}
.snow:nth-child(187) {
  opacity: 0.7129;
  transform: translate(36.3642vw, -10px) scale(0.4775);
  animation: fall-187 12s -6s linear infinite;
}
@keyframes fall-187 {
  52.779% {
    transform: translate(39.7097vw, 52.779vh) scale(0.4775);
  }
  to {
    transform: translate(38.03695vw, 100vh) scale(0.4775);
  }
}
.snow:nth-child(188) {
  opacity: 0.6252;
  transform: translate(28.3126vw, -10px) scale(0.5823);
  animation: fall-188 20s -27s linear infinite;
}
@keyframes fall-188 {
  65.885% {
    transform: translate(26.7688vw, 65.885vh) scale(0.5823);
  }
  to {
    transform: translate(27.5407vw, 100vh) scale(0.5823);
  }
}
.snow:nth-child(189) {
  opacity: 0.6156;
  transform: translate(15.8954vw, -10px) scale(0.5345);
  animation: fall-189 17s -23s linear infinite;
}
@keyframes fall-189 {
  64.613% {
    transform: translate(7.2123vw, 64.613vh) scale(0.5345);
  }
  to {
    transform: translate(11.55385vw, 100vh) scale(0.5345);
  }
}
.snow:nth-child(190) {
  opacity: 0.0601;
  transform: translate(25.1726vw, -10px) scale(0.7249);
  animation: fall-190 10s -4s linear infinite;
}
@keyframes fall-190 {
  74.8% {
    transform: translate(31.9493vw, 74.8vh) scale(0.7249);
  }
  to {
    transform: translate(28.56095vw, 100vh) scale(0.7249);
  }
}
.snow:nth-child(191) {
  opacity: 0.0561;
  transform: translate(18.626vw, -10px) scale(0.2794);
  animation: fall-191 30s -12s linear infinite;
}
@keyframes fall-191 {
  68.97% {
    transform: translate(23.5899vw, 68.97vh) scale(0.2794);
  }
  to {
    transform: translate(21.10795vw, 100vh) scale(0.2794);
  }
}
.snow:nth-child(192) {
  opacity: 0.8992;
  transform: translate(33.7569vw, -10px) scale(0.7801);
  animation: fall-192 19s -15s linear infinite;
}
@keyframes fall-192 {
  77.63% {
    transform: translate(42.4426vw, 77.63vh) scale(0.7801);
  }
  to {
    transform: translate(38.09975vw, 100vh) scale(0.7801);
  }
}
.snow:nth-child(193) {
  opacity: 0.193;
  transform: translate(3.6293vw, -10px) scale(0.1708);
  animation: fall-193 28s -29s linear infinite;
}
@keyframes fall-193 {
  67.558% {
    transform: translate(0.8559vw, 67.558vh) scale(0.1708);
  }
  to {
    transform: translate(2.2426vw, 100vh) scale(0.1708);
  }
}
.snow:nth-child(194) {
  opacity: 0.4162;
  transform: translate(27.1602vw, -10px) scale(0.3345);
  animation: fall-194 29s -19s linear infinite;
}
@keyframes fall-194 {
  52.937% {
    transform: translate(20.0127vw, 52.937vh) scale(0.3345);
  }
  to {
    transform: translate(23.58645vw, 100vh) scale(0.3345);
  }
}
.snow:nth-child(195) {
  opacity: 0.892;
  transform: translate(26.2516vw, -10px) scale(0.5945);
  animation: fall-195 12s -25s linear infinite;
}
@keyframes fall-195 {
  61.095% {
    transform: translate(29.4374vw, 61.095vh) scale(0.5945);
  }
  to {
    transform: translate(27.8445vw, 100vh) scale(0.5945);
  }
}
.snow:nth-child(196) {
  opacity: 0.3511;
  transform: translate(97.7467vw, -10px) scale(0.6858);
  animation: fall-196 21s -7s linear infinite;
}
@keyframes fall-196 {
  71.56% {
    transform: translate(102.0437vw, 71.56vh) scale(0.6858);
  }
  to {
    transform: translate(99.8952vw, 100vh) scale(0.6858);
  }
}
.snow:nth-child(197) {
  opacity: 0.6895;
  transform: translate(55.5666vw, -10px) scale(0.4778);
  animation: fall-197 20s -2s linear infinite;
}
@keyframes fall-197 {
  44.267% {
    transform: translate(62.6036vw, 44.267vh) scale(0.4778);
  }
  to {
    transform: translate(59.0851vw, 100vh) scale(0.4778);
  }
}
.snow:nth-child(198) {
  opacity: 0.9796;
  transform: translate(25.7771vw, -10px) scale(0.8737);
  animation: fall-198 21s -6s linear infinite;
}
@keyframes fall-198 {
  61.193% {
    transform: translate(23.8967vw, 61.193vh) scale(0.8737);
  }
  to {
    transform: translate(24.8369vw, 100vh) scale(0.8737);
  }
}
.snow:nth-child(199) {
  opacity: 0.0428;
  transform: translate(83.4462vw, -10px) scale(0.7259);
  animation: fall-199 20s -10s linear infinite;
}
@keyframes fall-199 {
  68.867% {
    transform: translate(87.2694vw, 68.867vh) scale(0.7259);
  }
  to {
    transform: translate(85.3578vw, 100vh) scale(0.7259);
  }
}
.snow:nth-child(200) {
  opacity: 0.1137;
  transform: translate(58.7526vw, -10px) scale(0.8374);
  animation: fall-200 17s -6s linear infinite;
}
@keyframes fall-200 {
  60.149% {
    transform: translate(55.5808vw, 60.149vh) scale(0.8374);
  }
  to {
    transform: translate(57.1667vw, 100vh) scale(0.8374);
  }
}
</style>