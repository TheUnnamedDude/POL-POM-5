/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.services;

import com.playonlinux.common.Progressable;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.common.api.services.RemoteAvailableInstallers;
import com.playonlinux.common.api.services.EventHandler;
import com.playonlinux.domain.Script;
import com.playonlinux.common.api.services.InstalledApplications;
import com.playonlinux.common.api.services.InstalledVirtualDrives;
import com.playonlinux.webservice.RemoteInstallerDownloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Scan
public class EventHandlerPlayOnLinuxImplementation implements EventHandler {

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private InstalledApplications installedApplications;
    private InstalledVirtualDrivesPlayOnLinuxImplementation virtualDrives;
    private RemoteAvailableInstallers remoteAvailableInstallers;
    private RemoteInstallerDownloader remoteInstallerDownloaderDownloader;

    public void runLocalScript(File scriptToRun) throws IOException {
        Script playonlinuxScript = Script.createInstance(scriptToRun);
        playOnLinuxBackgroundServicesManager.register(playonlinuxScript);
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxException {
        if(installedApplications == null) {
            installedApplications = new InstalledApplicationsPlayOnLinuxImplementation();
        }
        return this.installedApplications;
    }

    @Override
    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxException {
        if(virtualDrives == null) {
            virtualDrives = new InstalledVirtualDrivesPlayOnLinuxImplementation();
        }

        return this.virtualDrives;
    }

    /* Remote Installer Downloader */
    @Override
    public Progressable getRemoteInstallerDownloaderDownloader() {
        if(remoteInstallerDownloaderDownloader == null) {
            remoteInstallerDownloaderDownloader = new RemoteInstallerDownloader();
        }
        return remoteInstallerDownloaderDownloader;
    }

    @Override
    public void installProgram(String selectedItemLabel) {
        playOnLinuxBackgroundServicesManager.register(remoteInstallerDownloaderDownloader.createTask(selectedItemLabel));
    }

    @Override
    public RemoteAvailableInstallers getRemoteAvailableInstallers() {
        if(remoteAvailableInstallers == null) {
            try {
                remoteAvailableInstallers = new RemoteAvailableInstallersPlayOnLinuxImplementation();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return this.remoteAvailableInstallers;
    }

    @Override
    public void onApplicationStarted() throws MalformedURLException {
        // Will be implemented later
    }



}
