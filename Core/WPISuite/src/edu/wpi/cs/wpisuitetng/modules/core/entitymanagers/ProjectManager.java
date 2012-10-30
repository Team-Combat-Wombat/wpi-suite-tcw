package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.core.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ProjectManager implements EntityManager<Project>{

	Class<Project> project = Project.class;
	Gson gson;
	
	public ProjectManager()
	{
		gson = new Gson();
	}
	
	@Override
	public Project makeEntity(String content) {
		
		Project p;
		
		p = gson.fromJson(content, project);
		
		if(getEntity( ((Integer) p.getIdNum()).toString() ).length == 0)
		{
			save(p);
		}
		
		return p;
	}

	@Override
	public Project[] getEntity(String id) 
	{
		Project[] m = new Project[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll();
		}
		else
		{
			return DataStore.getDataStore().retrieve(project, "idNum", id).toArray(m);
		}
	}

	@Override
	public Project[] getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Project model) {
		DataStore.getDataStore().save(model);
		
	}

	@Override
	public boolean deleteEntity(String id)
	{
		DataStore data = DataStore.getDataStore();
		
		String s = data.delete(data.retrieve(project, "idNum", id).get(0));
		
		return (s.startsWith("Deleted")) ? true : false;
	}
	
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}


	

}
