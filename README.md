# WebDriver manager for java

[![](https://travis-ci.com/rosolko/wdm4j.svg?branch=master)](https://travis-ci.com/rosolko/wdm4j)
[![](https://sonarcloud.io/api/project_badges/measure?project=rosolko_wdm4j&metric=alert_status)](https://sonarcloud.io/dashboard?id=rosolko_wdm4j)
[![](https://sonarcloud.io/api/project_badges/measure?project=rosolko_wdm4j&metric=coverage)](https://sonarcloud.io/dashboard?id=rosolko_wdm4j)
[![](https://jitpack.io/v/rosolko/wdm4j.svg)](https://jitpack.io/#rosolko/wdm4j)

This small library aimed to automate the Selenium WebDriver binaries management inside a java project.

## Installation

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }

	dependencies {
	    implementation 'com.github.rosolko:wdm4j:master-SNAPSHOT'
	}

## Using

**Pick preset binary configuration**:

* `ChromeConfig`
* `EdgeConfig`
* `FirefoxConfig`
* `InternetExplorerConfig`
* `OperaConfig`
* `PhantomJsConfig`

Or implement your own from `CommonConfig` interface

**Configure based on selected configuration**:

    new WebDriverManager().setup(new ChromeConfig())
    
**Lock binary version**:

    new WebDriverManager().setup(new ChromeConfig(), "2.45")    
