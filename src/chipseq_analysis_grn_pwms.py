#!/usr/bin/python3

import json

table = json.load(open("grn_factors.json", 'r'))
OF = open("grn_pwms.tsv", 'w')
OF.write("PWM\tCoefficient\n")
for r in range(len(table["data"][3])):
    pwms = table["data"][3][r].split(",")
    coef = table["data"][4][r]
    for pwm in pwms:
        OF.write(pwm + "\t" + str(coef) + "\n")
OF.close()
