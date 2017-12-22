#!/usr/bin/env python

from setuptools import setup, find_packages

setup(
    name='PyProj',
    version='0.1',
    description='PyProj library',
    url='http://www.smartnet.com/',
    packages=find_packages(),
    include_package_data = True,
    package_data={'': []}
    )

