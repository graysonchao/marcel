#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys
import re
import uuid
import codecs


try:
    from pip.req import parse_requirements
    from setuptools import setup
    from setuptools.command.test import test as TestCommand
except ImportError:
    raise ImportError(
        "Please upgrade `setuptools` to the newest version via: "
        "`pip install -U setuptools`"
    )


here = os.path.abspath(os.path.dirname(__file__))


class PyTest(TestCommand):
    def finalize_options(self):
        TestCommand.finalize_options(self)
        self.test_args = ['--strict', '--verbose', '--tb=long', 'tests']
        self.test_suite = True

    def run_tests(self):
        import pytest
        errno = pytest.main(self.test_args)
        sys.exit(errno)


def read(*parts):
    # intentionally *not* adding an encoding option to open
    return codecs.open(os.path.join(here, *parts), 'r').read()


def find_version(*file_paths):
    version_file = read(*file_paths)
    version_match = re.search(r"^__version__ = ['\"]([^'\"]*)['\"]",
                              version_file, re.M)
    if version_match:
        return version_match.group(1)
    raise RuntimeError("Unable to find version string.")


def extract_requirements(path):
    return [str(ir.req) for ir in parse_requirements(path, session=uuid.uuid1())]


with open('README.md') as readme_file:
    readme = readme_file.read()


setup(
    platforms='any',
    name='marcel',
    version=find_version('marcel', '__init__.py'),
    description='Bot for https://halite.io/',
    long_description=readme,
    author='Marcel the bot',
    author_email='marcelthebot@marc.el',
    url='https://github.com/graysonchao/marcel/',
    packages=[
        'marcel',
    ],
    package_dir={'marcel': 'marcel'},
    include_package_data=True,
    install_requires=extract_requirements('requirements.txt'),
    test_requires=extract_requirements('dev-requirements.txt'),
    license='ISCL',
    keywords='marcel',
    classifiers=[
        'Development Status :: 2 - Pre-Alpha',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: ISC License (ISCL)',
        'Natural Language :: English',
        'Programming Language :: Python :: 3.5',
    ],
    test_suite='marcel.test.test_marcel',
    tests_require=['pytest'],
    cmdclass={'test': PyTest},
    zip_safe=False,
    extras_require={
        'testing': ['pytest', 'pytest-cov'],
    },
)
